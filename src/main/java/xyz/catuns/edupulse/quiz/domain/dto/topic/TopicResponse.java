package xyz.catuns.edupulse.quiz.domain.dto.topic;

import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;

import java.util.Set;
import java.util.UUID;
import java.util.Map;
import java.time.Instant;

public record TopicResponse(
        UUID id,
        String skill,
        Map<DifficultyLevel, Integer> questions,
        Set<String> tags,
        Instant createdAt,
        Instant updatedAt
) {
}
