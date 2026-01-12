package xyz.catuns.edupulse.quiz.domain.dto.question;

import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String text,
        DifficultyLevel difficulty,
        List<AnswerChoiceResponse> choices,
        String tag
) {
}
