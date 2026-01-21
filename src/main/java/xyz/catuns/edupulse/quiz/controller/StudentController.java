package xyz.catuns.edupulse.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.catuns.edupulse.quiz.domain.dto.student.CreateStudentRequest;
import xyz.catuns.edupulse.quiz.domain.dto.student.StudentResponse;
import xyz.catuns.edupulse.quiz.service.StudentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
@Tag(name = "Student API")
public class StudentController {

    private final StudentService studentService;


    @PostMapping(value = "")
    @Operation(
            summary = "Create Student",
            description = "REST API Post to CreateStudent")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status ACCEPTED")
    public ResponseEntity<StudentResponse> createStudent(
            @RequestBody CreateStudentRequest request
    ){
        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping(value = "/{name}")
    @Operation(
            summary = "Get Student by name",
            description = "REST API Get to Student")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<StudentResponse> getStudentByName(
            @PathVariable("name") String studentName
    ){
        StudentResponse response = studentService.getStudentByName(studentName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
