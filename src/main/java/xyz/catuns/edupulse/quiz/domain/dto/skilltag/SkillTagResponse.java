package xyz.catuns.edupulse.quiz.domain.dto.skilltag;

import java.util.UUID;

public record SkillTagResponse(
        UUID skillTagId,
        String skill,
        String tag,
        String displayName
) {
}
