package xyz.catuns.edupulse.quiz.domain.dto.session;

import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.List;
import java.util.UUID;

public record SessionSearchCriteria(
        UUID studentId,
        List<SessionEventType> hasStatus,
        List<SessionEventType> ignoreStatus,
        DifficultyLevel minDifficultyLevel,
        DifficultyLevel maxDifficultyLevel
) {
    public SessionSearchCriteria {
        if (minDifficultyLevel == null) {
            minDifficultyLevel = DifficultyLevel.BEGINNER;
        }
        if (maxDifficultyLevel == null) {
            maxDifficultyLevel = DifficultyLevel.ADVANCED;
        }
    }
}
