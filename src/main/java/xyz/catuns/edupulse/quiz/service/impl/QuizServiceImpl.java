package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswer;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerRequest;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;
import xyz.catuns.edupulse.quiz.domain.entity.AnswerChoice;
import xyz.catuns.edupulse.quiz.domain.entity.Session;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgress;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgressId;
import xyz.catuns.edupulse.quiz.domain.mapper.QuizMapper;
import xyz.catuns.edupulse.quiz.domain.repository.SessionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.StudentProgressRepository;
import xyz.catuns.edupulse.quiz.messaging.producer.QuizEventProducer;
import xyz.catuns.edupulse.quiz.service.QuizService;
import xyz.catuns.spring.base.exception.controller.BadRequestException;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final SessionRepository sessionRepository;
    private final StudentProgressRepository studentProgressRepository;
    private final QuizMapper quizMapper;
    private final QuizEventProducer quizEventProducer;

    @Override
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request) {
        // fetch session
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new BadRequestException("No session matching id " + request.sessionId()));

        if (session.getCurrentQuestion() == null) {
            throw new BadRequestException("No current question found");
        }

        AnswerChoice answerChoice = session.getCurrentQuestion().getAnswerChoices().stream()
                .filter(a -> a.getId().equals(request.answerId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid answer choice for question"));

        int attempts = incrementAttempt(session.getStudentId(), session.getCurrentQuestion().getId()).intValue();
        long timeSpentMs = Duration.between(Instant.now(), session.getUpdatedAt()).toMillis();

        // publish event
        QuizAnswer quizAnswer = quizMapper.buildQuizAnswerEvent(
                session,
                answerChoice,
                attempts,
                timeSpentMs
        );
        quizEventProducer.publishQuizAnswerEvent(quizAnswer);

        log.debug("Published answer (attempt {}) for session {}",
                quizAnswer.getAttemptNumber(),
                quizAnswer.getEnvelope().getSessionId());

        updateStudentProgress(
                session.getStudentId(),
                session.getCurrentQuestion().getTopic().getId(),
                session.getCurrentQuestion().isCorrect(request.answerId())
        );

        return quizMapper.toResponse(quizAnswer, session.getCurrentQuestion());
    }

    private void updateStudentProgress(UUID studentId, UUID topicId, boolean isCorrect) {
        StudentProgressId id = new StudentProgressId();
        id.setStudentId(studentId);
        id.setTopicId(topicId);

        StudentProgress studentProgress = studentProgressRepository.findById(id)
                .orElseGet(() -> {
                    StudentProgress newStudentProgress = new StudentProgress();
                    newStudentProgress.setId(id);
                    return newStudentProgress;
                });

        if (isCorrect) {
            studentProgress.incrementCorrectAttempt();
        } else {
            studentProgress.incrementAttempt();
        }

        studentProgressRepository.save(studentProgress);
    }

    private Long incrementAttempt(UUID studentId, UUID questionId) {
        String key = "quiz:attempt:" + studentId + ":" + questionId;
        boolean existed = redisTemplate.hasKey(key);
        Long attempts = redisTemplate.opsForValue().increment(key, 1L);
        if (!existed) {
            redisTemplate.expire(key, Duration.ofHours(1));
        }
        return attempts;
    }
}
