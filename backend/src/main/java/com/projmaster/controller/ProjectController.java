package com.projmaster.controller;

import com.projmaster.dto.request.ProjectCreateRequest;
import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.ProjectResponse;
import com.projmaster.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ApiResponse<List<ProjectResponse>> getMyProjects() {
        return ApiResponse.success(projectService.getMyProjects());
    }

    @GetMapping("/all")
    public ApiResponse<List<ProjectResponse>> getAllProjects() {
        return ApiResponse.success(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getProject(@PathVariable Long id) {
        return ApiResponse.success(projectService.getProjectById(id));
    }

    @PostMapping
    public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return ApiResponse.success(projectService.createProject(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long id, 
                                                      @Valid @RequestBody ProjectCreateRequest request) {
        return ApiResponse.success(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/archive")
    public ApiResponse<Void> archiveProject(@PathVariable Long id) {
        projectService.archiveProject(id);
        return ApiResponse.success();
    }

    @PostMapping("/{projectId}/members")
    public ApiResponse<Void> addMember(@PathVariable Long projectId,
                                       @RequestParam Long userId,
                                       @RequestParam(defaultValue = "MEMBER") String role) {
        projectService.addProjectMember(projectId, userId, role);
        return ApiResponse.success();
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ApiResponse<Void> removeMember(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeProjectMember(projectId, userId);
        return ApiResponse.success();
    }
}
