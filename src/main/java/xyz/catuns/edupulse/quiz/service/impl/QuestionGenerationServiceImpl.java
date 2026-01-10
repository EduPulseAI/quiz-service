package xyz.catuns.edupulse.quiz.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsRequest;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiQuestion;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiQuestionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;
import xyz.catuns.edupulse.quiz.domain.mapper.QuestionMapper;
import xyz.catuns.edupulse.quiz.domain.mapper.SkillTagMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.SkillTagRepository;
import xyz.catuns.edupulse.quiz.exception.GeminiGenerationException;
import xyz.catuns.edupulse.quiz.service.QuestionGenerationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionGenerationServiceImpl implements QuestionGenerationService {

    private final ChatClient chatClient;
    private final PromptTemplate promptTemplate;
    private final QuestionRepository questionRepository;
    private final SkillTagRepository skillTagRepository;
    private final ObjectMapper objectMapper;
    private final QuestionMapper questionMapper;
    private final SkillTagMapper skillTagMapper;

    @Override
    @Async("questionGeneratorExecutor")
    public CompletableFuture<GenerateQuestionsResponse> generateQuestions(GenerateQuestionsRequest request) {
        log.debug("Starting async question generation for topic: {} on thread: {}",
                request.topic(), Thread.currentThread().getName());

        try {
            // Generate questions from AI
            GeminiQuestionResponse geminiResponse = generateQuestionsFromAI(request);

            // Persist to database
            List<Question> persistedQuestions = persistQuestions(geminiResponse);

            // Map to response DTO
            GenerateQuestionsResponse response = mapToResponse(persistedQuestions);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            log.error("Error generating questions asynchronously", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private GenerateQuestionsResponse mapToResponse(List<Question> questions) {
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions were generated");
        }

        // Get skill tag from first question (all share the same skill tag)
        SkillTag skillTag = questions.getFirst().getSkillTag();

        SkillTagResponse skillTagDto = skillTagMapper.toResponse(skillTag);

        List<QuestionResponse> questionResponses = questionMapper.toResponseList(questions);

        return new GenerateQuestionsResponse(skillTagDto, questionResponses);
    }

    private List<Question> persistQuestions(GeminiQuestionResponse geminiResponse) {
        log.debug("Persisting {} questions to database", geminiResponse.questions().size());

        Set<String> tags = geminiResponse.questions().stream()
                .map(GeminiQuestion::tag)
                .map(SkillTagMapper.slug)
                .collect(Collectors.toSet());
        // Find or create skill tag
        SkillTag skillTag = findOrCreateSkillTag(
                geminiResponse.skillTag().skill(),
                tags
        );

        // Map and persist questions
        List<Question> questions = geminiResponse.questions().stream()
                .map(q -> questionMapper.toEntity(q, skillTag))
                .collect(Collectors.toList());

        return questionRepository.saveAll(questions);
    }

    private SkillTag findOrCreateSkillTag(String skill, Set<String> tags) {
        SkillTag skillTag = skillTagRepository.findBySkill(skill)
                .orElseGet(() -> {
                    SkillTag newTag = new SkillTag();
                    newTag.setSkill(skill);
                    return newTag;
                });
        skillTag.getTags().addAll(tags);
        return skillTagRepository.save(skillTag);
    }

    private GeminiQuestionResponse generateQuestionsFromAI(GenerateQuestionsRequest request) throws JsonProcessingException {
        // Create output converter for structured JSON response
        BeanOutputConverter<GeminiQuestionResponse> outputConverter =
                new BeanOutputConverter<>(GeminiQuestionResponse.class);

        // Build prompt parameters
        Map<String, Object> promptParams = buildPromptParameters(request, outputConverter);

        // Create and populate prompt template

        log.debug("Calling Vertex AI Gemini with prompt for {} questions", request.questionCount());

        // Call Vertex AI Gemini
        String response = chatClient.prompt()
                .user(userSpec -> userSpec.text(promptTemplate.render(promptParams)))
                .call()
                .content();

        if (response == null || response.isBlank()) {
            throw new GeminiGenerationException("Gemini returned empty response");
        }

        log.debug("Received response from Vertex AI, parsing structured output \n{}", response);

        // Convert JSON response to strongly-typed object
        return objectMapper.readValue(response, GeminiQuestionResponse.class);
    }

    private Map<String, Object> buildPromptParameters(GenerateQuestionsRequest request, BeanOutputConverter<GeminiQuestionResponse> outputConverter) {
        Map<String, Object> params = new HashMap<>();
        params.put("topic", request.topic());
        params.put("questionCount", request.questionCount());
        params.put("difficultyLevel", request.difficultyLevel().getDescription());
        params.put("existingQuestionsSection", "");
        params.put("format", outputConverter.getFormat());

        // Conditional course context section
        if (request.courseContext() != null && !request.courseContext().isBlank()) {
            params.put("courseContextSection",
                    "Course Context: " + request.courseContext());
        } else {
            params.put("courseContextSection", "");
        }

        // Conditional skill tags section
        if (request.skillTags() != null && !request.skillTags().isEmpty()) {
            params.put("skillTagsSection",
                    "Related Skills/Tags: " + String.join(", ", request.skillTags()));
        } else {
            params.put("skillTagsSection", "");
        }

        return params;
    }
}
