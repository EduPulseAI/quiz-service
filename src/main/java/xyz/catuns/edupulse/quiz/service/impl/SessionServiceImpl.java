package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.common.messaging.events.EventEnvelope;
import xyz.catuns.edupulse.common.messaging.events.session.SessionEvent;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Session;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;
import xyz.catuns.edupulse.quiz.domain.mapper.SessionMapper;
import xyz.catuns.edupulse.quiz.domain.repository.QuestionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.SessionRepository;
import xyz.catuns.edupulse.quiz.domain.repository.SkillTagRepository;
import xyz.catuns.edupulse.quiz.domain.specification.QuestionSpecification;
import xyz.catuns.edupulse.quiz.messaging.producer.QuizEventProducer;
import xyz.catuns.edupulse.quiz.service.SessionService;
import xyz.catuns.spring.base.exception.controller.NotFoundException;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper mapper;

    private final QuizEventProducer producer;

    private final SessionRepository repository;
    private final SkillTagRepository skillTagRepository;
    private final QuestionRepository questionRepository;

    @Override
    public StartSessionResponse startSession(StartSessionRequest request) {
        // Fetch SkillTag
        SkillTag skillTag = skillTagRepository.findById(request.skillTagId())
                .orElseThrow(() -> new NotFoundException("No skill tag with id %s"
                        .formatted(request.skillTagId())));

        // Fetch First question
        Question firstQuestion = questionRepository.findRandomBySkillTagId(request.skillTagId())
                .orElseThrow(() -> new NotFoundException("No question found for skill %s"
                        .formatted(skillTag.getSkill())));

        // Create session entity
        Session session = mapper.toEntity(request, firstQuestion);
        session = repository.save(session);

        // send session event
        SessionEvent event = mapper.toSessionEvent(session);
        producer.publishSessionEvent(event);

        return mapper.toResponse(session);
    }
}
