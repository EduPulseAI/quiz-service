package xyz.catuns.edupulse.quiz.domain.dto.session;

import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.UUID;

public record StartSessionResponse(
        UUID sessionId,
        Long startedAt,
        DifficultyLevel initialDifficulty,
        QuestionResponse firstQuestion
) {
}
