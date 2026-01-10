package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerRequest;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;

public interface QuizService {
    SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request);
}
