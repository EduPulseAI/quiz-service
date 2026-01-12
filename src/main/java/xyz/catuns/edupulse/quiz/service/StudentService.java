package xyz.catuns.edupulse.quiz.service;

import xyz.catuns.edupulse.quiz.domain.dto.student.CreateStudentRequest;
import xyz.catuns.edupulse.quiz.domain.dto.student.StudentResponse;

public interface StudentService {
    StudentResponse createStudent(CreateStudentRequest request);

    StudentResponse getStudentByName(String studentName);
}
