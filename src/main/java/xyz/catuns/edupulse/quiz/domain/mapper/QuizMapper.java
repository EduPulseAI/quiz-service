package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import xyz.catuns.edupulse.common.messaging.events.EventEnvelope;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswer;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizContext;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;
import xyz.catuns.edupulse.quiz.domain.entity.AnswerChoice;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Session;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;
import static xyz.catuns.edupulse.quiz.utils.NameCaseResolver.toKebabCase;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        unmappedTargetPolicy = IGNORE)
public abstract class QuizMapper {

    @Value("${spring.application.name}")
    private String applicationName;


    public EventEnvelope.Builder buildEventEnvelope(Session session) {
        return EventEnvelope.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSource(applicationName)
                .setTimestamp(Instant.now())
                .setSessionId(session.getId().toString())
                .setStudentId(session.getStudentId().toString());
    }

    public QuizAnswer buildQuizAnswerEvent(Session session, AnswerChoice answerChoice, int attempts, long timeSpent) {
        return QuizAnswer.newBuilder()
                .setQuestionId(session.getCurrentQuestion().getId().toString())
                .setSkillTag(buildSkillTag(session.getCurrentQuestion()))
                .setDifficultyLevel(session.getCurrentQuestion().getDifficultyLevel().getLevelValue())
                .setIsCorrect(answerChoice.isCorrect())
                .setAnswer(answerChoice.getValue())
                .setAttemptNumber(attempts)
                .setTimeSpentMs(timeSpent)
                .setContextualData(QuizContext.newBuilder()
                        .setHintsUsed(0)
                        .setPreviousAnswers(List.of())
                        .build())
                .setEnvelope(buildEventEnvelope(session)
                        .setType("quiz.answered")
                        .build())
                .build();
    }

    private String buildSkillTag(Question question) {
        return toKebabCase(question.getTopic().getSkill())
                + "."
                + toKebabCase(question.getTag());
    }

    @Mapping(target = "isCorrect", source = "quizAnswer.isCorrect")
    @Mapping(target = "attemptNumber", source = "quizAnswer.attemptNumber")
    @Mapping(target = "eventId", source = "quizAnswer.envelope.id")
    @Mapping(target = "correctAnswer", source = "question.correctAnswer.value")
    @Mapping(target = "explanation", source = "question.explanation")
    public abstract SubmitAnswerResponse toResponse(QuizAnswer quizAnswer, Question question);
}
