package xyz.catuns.edupulse.quiz.domain.dto.session;

import java.util.UUID;

public record StartSessionRequest(
        UUID studentId,
        UUID topicId
) {
}
