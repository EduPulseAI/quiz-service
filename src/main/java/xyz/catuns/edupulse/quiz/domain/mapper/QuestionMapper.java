package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.*;
import xyz.catuns.edupulse.quiz.domain.dto.question.AnswerChoiceResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiAnswerChoice;
import xyz.catuns.edupulse.quiz.domain.dto.question.gemini.GeminiQuestion;
import xyz.catuns.edupulse.quiz.domain.entity.AnswerChoice;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;

import java.util.List;
import java.util.Set;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE)
public interface QuestionMapper {

    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "text", source = "questionText")
    @Mapping(target = "choices", source = "answerChoices")
    QuestionResponse toResponse(Question question);

    List<AnswerChoiceResponse> toChoiceListResponse(Set<AnswerChoice> answerChoices);

    @Mapping(target = "choiceId", source = "id")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "isCorrect", expression = "java(answerChoice.isCorrect())")
    AnswerChoiceResponse toAnswerChoiceResponse(AnswerChoice answerChoice);

    @Mapping(target = "difficultyLevel", source = "q.difficultyLevel", qualifiedByName = "parseDifficultyLevel")
    @Mapping(target = "questionText", source = "q.questionText")
    @Mapping(target = "correctAnswer", ignore = true)
    @Mapping(target = "answerChoices", ignore = true)
    @Mapping(target = "skillTag", source = "skillTag")
    @Mapping(target = "explanation", source = "q.explanation")
    @Mapping(target = "createdBy", constant = "gemini")
    Question toEntity(GeminiQuestion q, SkillTag skillTag);

    @AfterMapping
    default void setCorrectAnswer(@MappingTarget Question target, GeminiQuestion q, SkillTag skillTag) {
        for (GeminiAnswerChoice geminiAnswerChoice : q.answerChoices()) {
            AnswerChoice choice = this.toAnswerChoice(geminiAnswerChoice);
            target.addAnswerChoice(choice);
            if (geminiAnswerChoice.correct()) {
                target.setCorrectAnswer(choice);
            }


        }
    }

    @Mapping(target = "value", source = "value")
    @Mapping(target = "question", ignore = true)
    AnswerChoice toAnswerChoice(GeminiAnswerChoice answerChoice);

    @Named("parseDifficultyLevel")
    default DifficultyLevel parseDifficultyLevel(String difficultyString) {
        if (difficultyString == null) {
//            log.warn("Difficulty level is null, defaulting to INTERMEDIATE");
            return DifficultyLevel.INTERMEDIATE;
        }

        // Normalize the string (uppercase, remove spaces/special chars)
        String normalized = difficultyString.toUpperCase()
                .replaceAll("[^A-Z]", "");

        try {
            return DifficultyLevel.valueOf(normalized);
        } catch (IllegalArgumentException e) {
//            log.warn("Could not parse difficulty level '{}', defaulting to INTERMEDIATE", difficultyString);
            return DifficultyLevel.INTERMEDIATE;
        }
    }

    List<QuestionResponse> toResponseList(List<Question> questions);
}
