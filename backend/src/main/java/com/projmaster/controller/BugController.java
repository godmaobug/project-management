package com.projmaster.controller;

import com.projmaster.dto.request.BugCreateRequest;
import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.BugResponse;
import com.projmaster.entity.Bug;
import com.projmaster.service.BugService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class BugController {

    private final BugService bugService;

    @GetMapping("/my")
    public ApiResponse<List<BugResponse>> getMyBugs() {
        return ApiResponse.success(bugService.getMyBugs());
    }

    @GetMapping("/project/{projectId}")
    public ApiResponse<List<BugResponse>> getByProject(@PathVariable Long projectId) {
        return ApiResponse.success(bugService.getBugsByProject(projectId));
    }

    @GetMapping("/{id}")
    public ApiResponse<BugResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(bugService.getBugById(id));
    }

    @PostMapping
    public ApiResponse<BugResponse> create(@Valid @RequestBody BugCreateRequest request) {
        return ApiResponse.success(bugService.createBug(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<BugResponse> update(@PathVariable Long id,
                                           @Valid @RequestBody BugCreateRequest request) {
        return ApiResponse.success(bugService.updateBug(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        bugService.deleteBug(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id,
                                          @RequestParam Bug.BugStatus status) {
        bugService.updateBugStatus(id, status);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/assignee")
    public ApiResponse<Void> updateAssignee(@PathVariable Long id,
                                            @RequestParam(required = false) Long assigneeId) {
        bugService.updateBugAssignee(id, assigneeId);
        return ApiResponse.success();
    }
}
