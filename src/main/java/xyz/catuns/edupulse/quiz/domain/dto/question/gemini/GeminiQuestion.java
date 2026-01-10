package xyz.catuns.edupulse.quiz.domain.dto.question.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List; /**
 * Individual question as returned by Gemini AI.
 */
public record GeminiQuestion(
        @JsonProperty("question_text")
        String questionText,

        @JsonProperty("difficulty_level")
        String difficultyLevel,

        @JsonProperty("answer_choices")
        List<GeminiAnswerChoice> answerChoices,

        @JsonProperty("explanation")
        String explanation,

        @JsonProperty("tag")
        String tag
) {
}
