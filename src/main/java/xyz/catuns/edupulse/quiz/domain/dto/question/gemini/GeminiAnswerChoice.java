package xyz.catuns.edupulse.quiz.domain.dto.question.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Answer choice as returned by Gemini AI.
 */
public record GeminiAnswerChoice(
        @JsonProperty("value")
        String value,

        @JsonProperty("correct")
        boolean correct

) {
}
