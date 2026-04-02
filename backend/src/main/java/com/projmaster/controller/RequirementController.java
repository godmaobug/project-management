package com.projmaster.controller;

import com.projmaster.dto.request.RequirementCreateRequest;
import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.RequirementResponse;
import com.projmaster.entity.Requirement;
import com.projmaster.service.RequirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequirementController {

    private final RequirementService requirementService;

    @GetMapping("/project/{projectId}")
    public ApiResponse<List<RequirementResponse>> getByProject(@PathVariable Long projectId) {
        return ApiResponse.success(requirementService.getRequirementsByProject(projectId));
    }

    @GetMapping("/project/{projectId}/unplanned")
    public ApiResponse<List<RequirementResponse>> getUnplanned(@PathVariable Long projectId) {
        return ApiResponse.success(requirementService.getUnplannedRequirements(projectId));
    }

    @GetMapping("/{id}")
    public ApiResponse<RequirementResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(requirementService.getRequirementById(id));
    }

    @PostMapping
    public ApiResponse<RequirementResponse> create(@Valid @RequestBody RequirementCreateRequest request) {
        return ApiResponse.success(requirementService.createRequirement(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<RequirementResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody RequirementCreateRequest request) {
        return ApiResponse.success(requirementService.updateRequirement(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id,
                                          @RequestParam Requirement.RequirementStatus status) {
        requirementService.updateRequirementStatus(id, status);
        return ApiResponse.success();
    }

    @PostMapping("/{requirementId}/assign-to-iteration")
    public ApiResponse<Void> assignToIteration(@PathVariable Long requirementId,
                                               @RequestParam Long iterationId) {
        requirementService.assignToIteration(requirementId, iterationId);
        return ApiResponse.success();
    }
}
