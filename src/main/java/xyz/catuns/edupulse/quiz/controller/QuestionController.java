package xyz.catuns.edupulse.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsRequest;
import xyz.catuns.edupulse.quiz.domain.dto.question.GenerateQuestionsResponse;
import xyz.catuns.edupulse.quiz.domain.dto.question.QuestionResponse;
import xyz.catuns.edupulse.quiz.domain.entity.DifficultyLevel;
import xyz.catuns.edupulse.quiz.service.QuestionGenerationService;
import xyz.catuns.edupulse.quiz.service.QuestionService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/questions")
@Tag(name = "Question API - Quiz")
public class QuestionController {

    private final QuestionGenerationService questionGenerationService;
    private final QuestionService questionService;

    
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

    @GetMapping(value = "/next")
    @Operation(
            summary = "Get Next Question",
            description = "REST API Get to NextQuestion")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<QuestionResponse> getNextQuestion(
            @RequestParam("sessionId") UUID sessionId,
            @RequestParam("difficulty") DifficultyLevel difficulty
    ) {
        QuestionResponse response = questionService.getNextQuestion(sessionId, difficulty);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
