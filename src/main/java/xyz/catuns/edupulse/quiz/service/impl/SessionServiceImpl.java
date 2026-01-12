package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEvent;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionSearchCriteria;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Session;
import xyz.catuns.edupulse.quiz.domain.entity.Topic;
import xyz.catuns.edupulse.quiz.domain.mapper.SessionMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.SessionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.TopicRepository;
import xyz.catuns.edupulse.quiz.domain.specification.SessionSpecification;
import xyz.catuns.edupulse.quiz.messaging.producer.QuizEventProducer;
import xyz.catuns.edupulse.quiz.service.SessionService;
import xyz.catuns.spring.base.exception.controller.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper mapper;

    private final QuizEventProducer producer;

    private final SessionRepository repository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;

    @Override
    public StartSessionResponse startSession(StartSessionRequest request) {
        // Fetch Topic
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NotFoundException("No skill tag with id %s"
                        .formatted(request.topicId())));

        // Fetch First question
        Question firstQuestion = questionRepository.findRandomByTopicId(request.topicId())
                .orElseThrow(() -> new NotFoundException("No question found for skill %s"
                        .formatted(topic.getSkill())));

        // Create session entity
        Session session = mapper.toEntity(request, firstQuestion);
        session = repository.save(session);

        // send session event
        SessionEvent event = mapper.toSessionEvent(session);
        producer.publishSessionEvent(event);

        return mapper.toStartSessionResponse(session);
    }

    @Override
    public List<SessionResponse> getAllSessions(SessionSearchCriteria criteria) {
        Specification<Session> specification = SessionSpecification.fromCriteria(criteria);
        List<Session> all = repository.findAll(specification);
        return mapper.toResponseList(all);
    }

    @Override
    public SessionResponse getSession(UUID sessionId) {
        Session session = this.repository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("No session with id"));

        return mapper.toResponse(session);
    }
}
