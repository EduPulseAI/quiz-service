package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import xyz.catuns.edupulse.common.messaging.events.EventEnvelope;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswer;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizContext;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerRequest;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;
import xyz.catuns.edupulse.quiz.domain.entity.AnswerChoice;
import xyz.catuns.edupulse.quiz.domain.entity.Question;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        unmappedTargetPolicy = IGNORE)
public abstract class QuizMapper {

    @Value("${spring.application.name}")
    private String applicationName;


    @Named("buildEventEnvelope")
    protected EventEnvelope.Builder buildEventEnvelope() {
        return EventEnvelope.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSource(applicationName)
                .setTimestamp(Instant.now());
    }

    public QuizAnswer toQuizAnswerEvent(SubmitAnswerRequest request, Question question, boolean isCorrect, long attempts) {
        String answer = question.getAnswerChoices().stream()
                .filter(a -> a.getId().equals(request.answerId()))
                .map(AnswerChoice::getValue)
                .findFirst()
                .orElse("");
        return QuizAnswer.newBuilder()
                .setQuestionId(question.getId().toString())
                .setSkillTag(buildSkillTag(question))
                .setDifficultyLevel(question.getDifficultyLevel().getLevelValue())
                .setIsCorrect(isCorrect)
                .setAnswer(answer)
                .setAttemptNumber((int) attempts)
                .setTimeSpentMs(request.timeSpent())
                .setContextualData(QuizContext.newBuilder()
                        .setHintsUsed(0)
                        .setPreviousAnswers(List.of())
                        .build())
                .setEnvelope(buildEventEnvelope()
                        .setType("quiz.answered")
                        .setSessionId(request.sessionId().toString())
                        .setStudentId(request.studentId().toString())
                        .build())
                .build();
    }

    private String buildSkillTag(Question question) {
        return SkillTagMapper.slug.apply(question.getSkillTag().getSkill())
                + "."
                + SkillTagMapper.slug.apply(question.getTag());
    }

    @Mapping(target = "isCorrect", source = "quizAnswer.isCorrect")
    @Mapping(target = "attemptNumber", source = "quizAnswer.attemptNumber")
    @Mapping(target = "eventId", source = "quizAnswer.envelope.id")
    @Mapping(target = "correctAnswer", source = "question.correctAnswer.value")
    @Mapping(target = "explanation", source = "question.explanation")
    public abstract SubmitAnswerResponse toResponse(QuizAnswer quizAnswer, Question question);
}
