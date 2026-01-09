package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.skilltag.CreateSkillTagRequest;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;

public interface SkillTagService {
    SkillTagResponse createSkillTag(CreateSkillTagRequest request);
}
