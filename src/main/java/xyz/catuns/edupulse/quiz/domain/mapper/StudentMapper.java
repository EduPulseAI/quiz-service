package xyz.catuns.edupulse.quiz.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.catuns.edupulse.quiz.domain.dto.student.CreateStudentRequest;
import xyz.catuns.edupulse.quiz.domain.dto.student.StudentResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Student;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = IGNORE)
public interface StudentMapper {

    @Mapping(target = "password", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", expression = "java(request.name() + \"@edupulse.com\")")
    Student toEntity(CreateStudentRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    StudentResponse toResponse(Student student);
}
