package xyz.catuns.edupulse.quiz.domain.entity;

import lombok.Getter;

@Getter
public enum DifficultyLevel {

    BEGINNER(1, "Beginner (foundational concepts)"),
    EASY(2, "Easy (basic application)"),
    INTERMEDIATE(3, "Intermediate (requires analysis)"),
    HARD(4, "Hard (complex problem-solving)"),
    ADVANCED(5, "Advanced (expert-level synthesis)");

    private final int levelValue;
    private final String description;

    DifficultyLevel(int levelValue, String description) {
        this.levelValue = levelValue;
        this.description = description;
    }
}
