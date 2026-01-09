package xyz.catuns.edupulse.quiz.domain.dto.question;

import jakarta.validation.constraints.*;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.List;
import java.util.UUID;

public record GenerateQuestionsRequest(

        @NotBlank(message = "Topic must not be empty")
        @Size(max = 200, message = "Topic must not exceed 200 characters")
        String topic,

        @NotNull(message = "Question count is required")
        @Min(value = 1, message = "Must generate at least 1 question")
        @Max(value = 50, message = "Cannot generate more than 50 questions at once")
        int questionCount,

        @NotNull(message = "Difficulty level is required")
        DifficultyLevel difficultyLevel,

        List<String> skillTags,

        @Size(max = 500, message = "Course context must not exceed 500 characters")
        String courseContext
) {
}
