package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsRequest;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsResponse;

import java.util.concurrent.CompletableFuture;

public interface QuestionGenerationService {

    CompletableFuture<GenerateQuestionsResponse> generateQuestions(GenerateQuestionsRequest request);
}
