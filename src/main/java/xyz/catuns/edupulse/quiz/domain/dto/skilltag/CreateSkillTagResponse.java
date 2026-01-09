package xyz.catuns.edupulse.quiz.domain.dto.skilltag;

import java.util.UUID;

public record CreateSkillTagResponse(
        UUID skillTagId,
        String displayName
) {
}
