package xyz.catuns.edupulse.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionResponse;
import xyz.catuns.edupulse.quiz.domain.dto.session.SessionSearchCriteria;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionRequest;
import xyz.catuns.edupulse.quiz.domain.dto.session.StartSessionResponse;
import xyz.catuns.edupulse.quiz.service.SessionService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/sessions")
@Tag(name = "Session API")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping(value = "/start")
    @Operation(
            summary = "Start Session",
            description = "Starts Session event")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED")
    public ResponseEntity<StartSessionResponse> startSession(
            @RequestBody StartSessionRequest request
    ){
        StartSessionResponse response = sessionService.startSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "")
    @Operation(
            summary = "Get All Sessions",
            description = "Search for Sessions based on criteria")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<List<SessionResponse>> getAllSessions(
            @ModelAttribute SessionSearchCriteria criteria
    ){

        List<SessionResponse> response = sessionService.getAllSessions(criteria);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Get Session by session id",
            description = "REST API to get Session")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<SessionResponse> getSession(
            @PathVariable("id") UUID sessionId
    ){

        SessionResponse response = sessionService.getSession(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
