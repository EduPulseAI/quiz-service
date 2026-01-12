package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.common.messaging.events.quiz.QuizAnswer;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerRequest;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgress;
import xyz.catuns.edupulse.quiz.domain.entity.StudentProgressId;
import xyz.catuns.edupulse.quiz.domain.mapper.QuizMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.StudentProgressRepository;
import xyz.catuns.edupulse.quiz.messaging.producer.QuizEventProducer;
import xyz.catuns.edupulse.quiz.service.QuizService;
import xyz.catuns.spring.base.exception.controller.NotFoundException;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final QuestionRepository questionRepository;
    private final StudentProgressRepository studentProgressRepository;
    private final QuizMapper quizMapper;
    private final QuizEventProducer quizEventProducer;

    @Override
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request) {
        // fetch question
        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new NotFoundException("No question with id"));

        boolean isCorrect = question.getCorrectAnswer().getId()
                .equals(request.answerId());
        long attempts = incrementAttempt(request.studentId(), question.getId());

        // publish event
        QuizAnswer quizAnswer = quizMapper.toQuizAnswerEvent(
                request,
                question,
                isCorrect,
                attempts);
        quizEventProducer.publishQuizAnswerEvent(quizAnswer);

        updateStudentProgress(
                request.studentId(),
                question.getTopic().getId(),
                isCorrect);


        return quizMapper.toResponse(quizAnswer, question);
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
