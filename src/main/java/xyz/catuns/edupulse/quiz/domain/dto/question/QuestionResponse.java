package xyz.catuns.edupulse.quiz.domain.dto.question;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID questionId,
        String text,
        List<AnswerChoiceResponse> choices
) {
}
