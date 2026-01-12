package xyz.catuns.edupulse.quiz.domain.dto.question.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeminiTopic(
        @JsonProperty("skill")
        String skill
) {
}
