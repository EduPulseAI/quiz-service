package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.CreateSkillTagRequest;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;
import xyz.catuns.edupulse.quiz.domain.entity.SkillTag;

import java.util.function.Function;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE)
public interface SkillTagMapper {

    Function<String, String> slug = (text) -> text.replaceAll("\\W+", "-")
            .toLowerCase();

    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "skill", source = "skill")
    @Mapping(target = "tags", ignore = true)
    SkillTag toEntity(CreateSkillTagRequest request);

    @Named("asDisplayName")
    default String asDisplayName(SkillTag skillTag) {
        return slug.apply(skillTag.getSkill()) + "." +
                slug.apply(String.join(",", skillTag.getTags()));

    }

    @Mapping(target = "skillTagId", source = "id")
    @Mapping(target = "skill", source = "skill")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "displayName", source = ".", qualifiedByName = "asDisplayName")
    SkillTagResponse toResponse(SkillTag skillTag);
}
