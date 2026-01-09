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
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;
import xyz.catuns.edupulse.quiz.service.SessionService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/sessions")
@Tag(name = "Quiz - Session API")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping(value = "/start")
    @Operation(
            summary = "Start Session",
            description = "REST API Post to StartSession")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED")
    public ResponseEntity<StartSessionResponse> startSession(
            @RequestBody StartSessionRequest request
    ){
        StartSessionResponse response = sessionService.startSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
