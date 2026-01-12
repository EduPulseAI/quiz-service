package xyz.catuns.edupulse.quiz.domain.dto.question;

import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

public record QuestionDifficultyCount(
        DifficultyLevel difficulty,
        Integer count
) {
}
