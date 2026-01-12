package xyz.catuns.edupulse.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.catuns.edupulse.quiz.domain.dto.student.CreateStudentRequest;
import xyz.catuns.edupulse.quiz.domain.dto.student.StudentResponse;
import xyz.catuns.edupulse.quiz.domain.entity.Student;
import xyz.catuns.edupulse.quiz.domain.mapper.StudentMapper;
import xyz.catuns.edupulse.quiz.domain.repository.StudentRepository;
import xyz.catuns.edupulse.quiz.service.StudentService;
import xyz.catuns.spring.base.exception.controller.NotFoundException;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper mapper;
    private final StudentRepository repository;

    @Override
    public StudentResponse createStudent(CreateStudentRequest request) {
        Student student = mapper.toEntity(request);
        student = repository.save(student);
        return mapper.toResponse(student);
    }

    @Override
    public StudentResponse getStudentByName(String studentName) {
        Student student = this.repository.findByName(studentName)
                .orElseThrow(() -> new NotFoundException("No student named " + studentName));
        return mapper.toResponse(student);
    }
}
