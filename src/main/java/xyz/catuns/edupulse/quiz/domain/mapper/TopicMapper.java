package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import xyz.catuns.edupulse.quiz.domain.dto.topic.CreateTopicRequest;
import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;
import xyz.catuns.edupulse.quiz.domain.entity.Question;
import xyz.catuns.edupulse.quiz.domain.entity.Topic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE)
public interface TopicMapper {

    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "skill", source = "skill")
    Topic toEntity(CreateTopicRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "skill", source = "skill")
    @Mapping(target = "questions",source = "questions", qualifiedByName = "getQuestionDifficultyCounts")
    @Mapping(target = "tags", source = "questions", qualifiedByName = "getTagsFromQuestion")
    TopicResponse toResponse(Topic topic);

    @Named("getTagsFromQuestion")
    default Set<String> getTagsFromQuestion(List<Question> questions) {
        return questions.stream()
                .map(Question::getTag)
                .map(tag -> tag.split(",\\s*"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    @Named("getQuestionDifficultyCounts")
    default Map<DifficultyLevel, Integer> getQuestionDifficultyCounts(List<Question> questions) {
        Map<DifficultyLevel, Integer> collect = questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getDifficultyLevel,
                        Collectors.summingInt(q -> 1)
                ));
        for(DifficultyLevel d : DifficultyLevel.values()) {
            collect.putIfAbsent(d, 0);
        }

        return collect;
    }
}
