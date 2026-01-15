package xyz.catuns.edupulse.quiz.domain.dto.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;

import java.util.List;
import java.util.UUID;

public record SessionSearchCriteria(
        UUID studentId,
        List<SessionEventType> hasStatus,
        List<SessionEventType> ignoreStatus,

        @Min(value = 1)
        @Max(value = 5)
        Integer minDifficultyLevel,

        @Min(value = 1)
        @Max(value = 5)
        Integer maxDifficultyLevel
) {
}
