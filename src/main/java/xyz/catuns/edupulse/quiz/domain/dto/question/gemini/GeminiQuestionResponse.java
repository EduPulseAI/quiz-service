package xyz.catuns.edupulse.quiz.domain.dto.question.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeminiQuestionResponse(
        @JsonProperty("topic")
        GeminiTopic topic,

        @JsonProperty("questions")
        List<GeminiQuestion> questions
) {
}

