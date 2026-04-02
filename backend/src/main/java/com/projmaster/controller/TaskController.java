package com.projmaster.controller;

import com.projmaster.dto.request.TaskCreateRequest;
import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.TaskResponse;
import com.projmaster.entity.Task;
import com.projmaster.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/my")
    public ApiResponse<List<TaskResponse>> getMyTasks() {
        return ApiResponse.success(taskService.getMyTasks());
    }

    @GetMapping("/project/{projectId}")
    public ApiResponse<List<TaskResponse>> getByProject(@PathVariable Long projectId) {
        return ApiResponse.success(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/iteration/{iterationId}")
    public ApiResponse<List<TaskResponse>> getByIteration(@PathVariable Long iterationId) {
        return ApiResponse.success(taskService.getTasksByIteration(iterationId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(taskService.getTaskById(id));
    }

    @PostMapping
    public ApiResponse<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.success(taskService.createTask(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> update(@PathVariable Long id,
                                            @Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.success(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id,
                                          @RequestParam Task.TaskStatus status) {
        taskService.updateTaskStatus(id, status);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/assignee")
    public ApiResponse<Void> updateAssignee(@PathVariable Long id,
                                            @RequestParam(required = false) Long assigneeId) {
        taskService.updateTaskAssignee(id, assigneeId);
        return ApiResponse.success();
    }
}
