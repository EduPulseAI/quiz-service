package xyz.catuns.edupulse.quiz.domain.dto.session;

import jakarta.validation.constraints.NotNull;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;

import java.util.UUID;

public record SendSessionEventRequest(
        @NotNull
        UUID sessionId,
        @NotNull
        SessionEventType eventType,
        String pageId,
        Long dwellTimeMs
) {
}
