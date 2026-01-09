package xyz.catuns.edupulse.quiz.domain.entity;

import lombok.Getter;

@Getter
public enum DifficultyLevel {

    BEGINNER(1),
    EASY(2),
    INTERMEDIATE(3),
    HARD(4),
    ADVANCED(5);

    private final int levelValue;

    DifficultyLevel(int levelValue) {
        this.levelValue = levelValue;
    }
}
