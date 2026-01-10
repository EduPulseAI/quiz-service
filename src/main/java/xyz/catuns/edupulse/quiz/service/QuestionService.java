package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.UUID;

public interface QuestionService {
    QuestionResponse getNextQuestion(UUID sessionId, DifficultyLevel difficulty);
}
