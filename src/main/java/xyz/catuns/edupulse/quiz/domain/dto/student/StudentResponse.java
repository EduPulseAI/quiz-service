package xyz.catuns.edupulse.quiz.domain.dto.student;

import java.util.UUID;

public record StudentResponse(
        UUID id,
        String email,
        String name
) {
}
