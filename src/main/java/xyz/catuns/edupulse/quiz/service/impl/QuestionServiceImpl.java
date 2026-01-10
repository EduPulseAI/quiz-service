package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Session;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;
import xyz.catuns.edupulse.quiz.domain.mapper.QuestionMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.SessionRepository;
import xyz.catuns.edupulse.quiz.service.QuestionService;
import xyz.catuns.spring.base.exception.controller.NotFoundException;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final RedisTemplate<String, String> redis;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionResponse getNextQuestion(UUID sessionId, DifficultyLevel difficulty) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException(("No session with id")));

        // Get recent question ids from redis
        String key = "recent_questions:" + session.getStudentId();

        List<String> recentIds = redis.opsForList()
                .range(key, 0, -1);

        Question lastQuestion = session.getCurrentQuestion();
        if (recentIds == null || recentIds.isEmpty()) {
            recentIds = List.of(lastQuestion.getId().toString());
        }

        Question nextQuestion = findNextQuestion(
                lastQuestion.getSkillTag(),
                lastQuestion.getDifficultyLevel(),
                recentIds);

        // update session
        session.setCurrentQuestion(nextQuestion);
        session.setCurrentDifficulty(nextQuestion.getDifficultyLevel());
        sessionRepository.save(session);

        // Add to recent list
        redis.opsForList().leftPush(key, nextQuestion.getId().toString());
        redis.opsForList().trim(key, 0, 4);  // Keep last 5
        redis.expire(key, Duration.ofHours(1));

        return questionMapper.toResponse(nextQuestion);
    }

    private Question findNextQuestion(SkillTag skillTag, DifficultyLevel difficultyLevel, List<String> recentIds) {
        return questionRepository
                .findNextQuestion(
                        skillTag.getId(),
                        difficultyLevel.ordinal(),
                        recentIds)
                .orElseThrow(() -> new NotFoundException(
                        "No next question for skill %s and difficulty %s "
                                .formatted(
                                        skillTag.getSkill(),
                                        difficultyLevel.getDescription())));
    }
}
