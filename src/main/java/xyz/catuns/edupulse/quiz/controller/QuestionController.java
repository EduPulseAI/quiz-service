package xyz.catuns.edupulse.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsRequest;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsResponse;
import xyz.catuns.edupulse.quiz.service.QuestionGenerationService;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/questions")
@Tag(name = "Question API - Quiz")
public class QuestionController {

    private final QuestionGenerationService questionGenerationService;

    
    @PostMapping(value = "/generate")
    @Operation(
            summary = "Generate Questions",
            description = "REST API Post to GenerateQuestions")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED")
    public ResponseEntity<CompletableFuture<GenerateQuestionsResponse>> generateQuestions(
            @RequestBody GenerateQuestionsRequest request
    ){
        CompletableFuture<GenerateQuestionsResponse> response = questionGenerationService.generateQuestions(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
