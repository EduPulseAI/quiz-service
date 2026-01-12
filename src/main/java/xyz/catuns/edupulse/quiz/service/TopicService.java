package xyz.catuns.edupulse.quiz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.catuns.edupulse.quiz.domain.dto.topic.CreateTopicRequest;
import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;

public interface TopicService {
    TopicResponse createTopic(CreateTopicRequest request);

    Page<TopicResponse> getTopics(Pageable pageable);
}
