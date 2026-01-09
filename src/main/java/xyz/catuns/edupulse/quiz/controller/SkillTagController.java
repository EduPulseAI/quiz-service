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
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.CreateSkillTagRequest;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.CreateSkillTagResponse;
import xyz.catuns.edupulse.quiz.domain.dto.skilltag.SkillTagResponse;
import xyz.catuns.edupulse.quiz.service.SkillTagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz/skill-tags")
@Tag(name = "Skill Tag API - Quiz Service")
public class SkillTagController {

    private final SkillTagService skillTagService;

    @PostMapping(value = "")
    @Operation(
            summary = "Create SkillTag",
            description = "REST API Post to CreateSkillTag")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED")
    public ResponseEntity<SkillTagResponse> createSkillTag(
            @RequestBody CreateSkillTagRequest request
    ){
        SkillTagResponse response = skillTagService.createSkillTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
