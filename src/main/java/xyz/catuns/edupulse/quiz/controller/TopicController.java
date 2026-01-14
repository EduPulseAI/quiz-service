package xyz.catuns.edupulse.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.catuns.edupulse.quiz.domain.dto.topic.CreateTopicRequest;
import xyz.catuns.edupulse.quiz.domain.dto.topic.TopicResponse;
import xyz.catuns.edupulse.quiz.service.TopicService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/topics")
@Tag(name = "Topic API")
public class TopicController {

    private final TopicService topicService;

    @PostMapping(value = "")
    @Operation(
            summary = "Create Topic",
            description = "REST API Post to CreateTopic")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED")
    public ResponseEntity<TopicResponse> createTopic(
            @RequestBody CreateTopicRequest request
    ){
        TopicResponse response = topicService.createTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping(value = "")
    @Operation(
            summary = "Get Topics",
            description = "REST API Get to GetTopics")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK")
    public ResponseEntity<Page<TopicResponse>> getTopics(
            @PageableDefault Pageable pageable
    ){
        Page<TopicResponse> response = topicService.getTopics(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
