package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import xyz.catuns.edupulse.common.messaging.events.EventEnvelope;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEvent;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Session;

import java.util.UUID;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        uses = QuestionMapper.class,
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        unmappedTargetPolicy = IGNORE)
public abstract class SessionMapper {

    @Value("${spring.application.name}")
    private String applicationName;


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "studentId", source = "request.studentId")
    @Mapping(target = "instructorId", ignore = true)
    @Mapping(target = "currentQuestion", source = "firstQuestion")
    @Mapping(target = "currentDifficulty", source = "firstQuestion.difficultyLevel")
    @Mapping(target = "startedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "endedAt", ignore = true)
    @Mapping(target = "status", constant = "STARTED")
    public abstract Session toEntity(StartSessionRequest request, Question firstQuestion);

    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "startedAt", expression = "java(session.getStartedAt().toEpochMilli())")
    @Mapping(target = "initialDifficulty", source = "currentDifficulty")
    @Mapping(target = "firstQuestion", source = "currentQuestion")
    public abstract StartSessionResponse toResponse(Session session);

    public SessionEvent toSessionEvent(Session session) {
        EventEnvelope eventEnvelope = buildEventEnvelope(session);
        return SessionEvent.newBuilder()
                .setEnvelope(eventEnvelope)
                .setEventType(session.getStatus())
                .build();
    }

    @Named("buildEventEnvelope")
    protected EventEnvelope buildEventEnvelope(Session session) {
        return EventEnvelope.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSource(applicationName)
                .setType("session." + session.getStatus().name().toLowerCase())
                .setTimestamp(session.getStartedAt())
                .setStudentId(session.getStudentId().toString())
                .setSessionId(session.getId().toString())
                .build();
    }
}
