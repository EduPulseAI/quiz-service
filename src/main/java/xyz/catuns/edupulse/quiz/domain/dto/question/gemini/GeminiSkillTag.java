package xyz.catuns.edupulse.quiz.domain.dto.question.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeminiSkillTag(
        @JsonProperty("skill")
        String skill
) {
}
