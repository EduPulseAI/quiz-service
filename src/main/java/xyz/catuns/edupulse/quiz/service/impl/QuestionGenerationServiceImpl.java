package xyz.catuns.edupulse.quiz.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsRequest;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiQuestion;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiQuestionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Topic;
import xyz.catuns.edupulse.quiz.domain.mapper.QuestionMapper;
import xyz.catuns.edupulse.quiz.domain.mapper.TopicMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.TopicRepository;
import xyz.catuns.edupulse.quiz.service.QuestionGenerationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionGenerationServiceImpl implements QuestionGenerationService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/question-generation.st")
    private Resource questionGenerationPromptResource;

    /* repos */
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;

    /* mappers */
    private final QuestionMapper questionMapper;
    private final TopicMapper topicMapper;

    @Override
    @Async("questionGeneratorExecutor")
    public CompletableFuture<GenerateQuestionsResponse> generateQuestions(GenerateQuestionsRequest request) {
        log.debug("Starting async question generation for topic: {} on thread: {}",
                request.topic(), Thread.currentThread().getName());

        try {
            // Generate questions
            GeminiQuestionResponse geminiResponse = generateQuestionsFromAI(request);

            // Persist to database
            Topic topic = findOrCreateTopic(geminiResponse.topic().skill());
            List<Question> persistedQuestions = persistQuestions(geminiResponse.questions(), topic);

            // Map to response DTO
            GenerateQuestionsResponse response = mapToResponse(persistedQuestions, topic);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            log.error("Error generating questions asynchronously", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private GenerateQuestionsResponse mapToResponse(List<Question> questions, Topic topic) {
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions were generated");
        }

        TopicResponse topicDto = topicMapper.toResponse(topic);

        List<QuestionResponse> questionResponses = questionMapper.toResponseList(questions);

        return new GenerateQuestionsResponse(topicDto, questionResponses);
    }

    private List<Question> persistQuestions(List<GeminiQuestion> geminiQuestions, Topic topic) {
        log.debug("Persisting {} questions to database", geminiQuestions.size());

        // Map and persist questions
        List<Question> questions = geminiQuestions.stream()
                .map(q -> questionMapper.toEntity(q, topic))
                .collect(Collectors.toList());

        return questionRepository.saveAll(questions);
    }

    private Topic findOrCreateTopic(String skill) {
        return topicRepository.findBySkill(skill)
                .orElseGet(() -> {
                    Topic topic = new Topic();
                    topic.setSkill(skill);
                    return topicRepository.save(topic);
                });
    }

    private GeminiQuestionResponse generateQuestionsFromAI(GenerateQuestionsRequest request) {
        // Build prompt parameters
        PromptTemplate promptTemplate = new PromptTemplate(questionGenerationPromptResource);
        Map<String, Object> promptParams = buildPromptParameters(request);

        return  chatClient.prompt()
                .user(userSpec -> userSpec.text(promptTemplate.render(promptParams)))
                .call()
                .entity(GeminiQuestionResponse.class);
    }

    private Map<String, Object> buildPromptParameters(GenerateQuestionsRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("topic", request.topic());
        params.put("questionCount", request.questionCount());
        params.put("difficultyLevel", request.difficultyLevel().getDescription());
        params.put("existingQuestionsSection", "");

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
