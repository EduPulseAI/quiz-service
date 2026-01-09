package xyz.catuns.edupulse.quiz.domain.dto.question;

import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;

import java.util.List;

public record GenerateQuestionsResponse(
        SkillTagResponse skillTag,
        List<QuestionResponse> questions
) {
}
