package xyz.catuns.edupulse.quiz.domain.dto.quiz;

import java.util.UUID;

public record SubmitAnswerResponse(
        boolean isCorrect,
        String correctAnswer,
        String explanation,
        long attemptNumber,
        UUID eventId
) {
}
