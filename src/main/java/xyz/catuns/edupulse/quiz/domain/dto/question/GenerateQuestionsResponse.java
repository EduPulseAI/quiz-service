package xyz.catuns.edupulse.quiz.domain.dto.question;

import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;

import java.util.List;

public record GenerateQuestionsResponse(
        TopicResponse topic,
        List<QuestionResponse> questions
) {
}
