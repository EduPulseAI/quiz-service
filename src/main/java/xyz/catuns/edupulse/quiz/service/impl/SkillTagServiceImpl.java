package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.CreateSkillTagRequest;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;
import xyz.catuns.edupulse.quiz.domain.mapper.SkillTagMapper;
import xyz.catuns.edupulse.quiz.domain.repository.SkillTagRepository;
import xyz.catuns.edupulse.quiz.service.SkillTagService;

@Service
@RequiredArgsConstructor
public class SkillTagServiceImpl implements SkillTagService {

    private final SkillTagMapper mapper;
    private final SkillTagRepository repository;

    @Override
    public SkillTagResponse createSkillTag(CreateSkillTagRequest request) {
        SkillTag skillTag = mapper.toEntity(request);
        skillTag = repository.save(skillTag);
        return mapper.toResponse(skillTag);
    }
}
