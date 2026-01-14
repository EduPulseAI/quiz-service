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
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerRequest;
import xyz.catuns.edupulse.quiz.domain.dto.quiz.SubmitAnswerResponse;
import xyz.catuns.edupulse.quiz.service.QuizService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz")
@Tag(name = "Quiz API")
public class QuizController {

    private final QuizService quizService;

    @PostMapping(value = "/answer/submit")
    @Operation(
            summary = "Submit Answer",
            description = "REST API Post Submit Answer")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(
            @RequestBody SubmitAnswerRequest request
    ) {
        SubmitAnswerResponse response = quizService.submitAnswer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
