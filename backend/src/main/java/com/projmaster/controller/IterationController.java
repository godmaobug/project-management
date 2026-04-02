package com.projmaster.controller;

import com.projmaster.dto.request.IterationCreateRequest;
import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.IterationResponse;
import com.projmaster.entity.Iteration;
import com.projmaster.service.IterationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iterations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class IterationController {

    private final IterationService iterationService;

    @GetMapping("/project/{projectId}")
    public ApiResponse<List<IterationResponse>> getByProject(@PathVariable Long projectId) {
        return ApiResponse.success(iterationService.getIterationsByProject(projectId));
    }

    @GetMapping("/{id}")
    public ApiResponse<IterationResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(iterationService.getIterationById(id));
    }

    @PostMapping
    public ApiResponse<IterationResponse> create(@Valid @RequestBody IterationCreateRequest request) {
        return ApiResponse.success(iterationService.createIteration(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<IterationResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody IterationCreateRequest request) {
        return ApiResponse.success(iterationService.updateIteration(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        iterationService.deleteIteration(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id,
                                          @RequestParam Iteration.IterationStatus status) {
        iterationService.updateIterationStatus(id, status);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/start")
    public ApiResponse<Void> startIteration(@PathVariable Long id) {
        iterationService.startIteration(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<Void> completeIteration(@PathVariable Long id) {
        iterationService.completeIteration(id);
        return ApiResponse.success();
    }
}
