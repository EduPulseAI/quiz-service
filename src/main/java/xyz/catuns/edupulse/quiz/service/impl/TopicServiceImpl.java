package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.topic.CreateTopicRequest;
import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Topic;
import xyz.catuns.edupulse.quiz.domain.mapper.TopicMapper;
import xyz.catuns.edupulse.quiz.domain.repository.TopicRepository;
import xyz.catuns.edupulse.quiz.service.TopicService;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicMapper mapper;
    private final TopicRepository repository;

    @Override
    public TopicResponse createTopic(CreateTopicRequest request) {
        Topic topic = mapper.toEntity(request);
        topic = repository.save(topic);
        return mapper.toResponse(topic);
    }

    @Override
    public Page<TopicResponse> getTopics(Pageable pageable) {
        Page<Topic> all = repository.findAll(pageable);
        return all.map(mapper::toResponse);
    }
}
