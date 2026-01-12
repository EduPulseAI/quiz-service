package xyz.catuns.edupulse.quiz.domain.dto.session;

import xyz.catuns.edupulse.common.messaging.events.session.SessionEventType;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.UUID;

public record SessionResponse(
        UUID id,
        UUID studentId,
        QuestionResponse currentQuestion,
        DifficultyLevel currentDifficulty,
        SessionEventType status
) {
}
