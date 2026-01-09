package xyz.catuns.edupulse.quiz.domain.dto.question;

import java.util.UUID;

public record AnswerChoiceResponse(
        UUID choiceId,
        String value,
        boolean isCorrect
) {
}
