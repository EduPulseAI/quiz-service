package xyz.catuns.edupulse.quiz.domain.dto.skilltag;

import java.util.Set;
import java.util.UUID;

public record SkillTagResponse(
        UUID skillTagId,
        String skill,
        Set<String> tags,
        String displayName
) {
}
