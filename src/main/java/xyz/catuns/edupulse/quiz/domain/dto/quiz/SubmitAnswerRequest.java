package xyz.catuns.edupulse.quiz.domain.dto.quiz;

import java.util.UUID;

public record SubmitAnswerRequest(
        UUID sessionId,
        UUID answerId
) {
}
