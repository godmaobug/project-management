package com.projmaster.service;

import com.projmaster.dto.request.TaskCreateRequest;
import com.projmaster.dto.response.TaskResponse;
import com.projmaster.entity.Task;

import java.util.List;

public interface TaskService {

    List<TaskResponse> getTasksByProject(Long projectId);

    List<TaskResponse> getTasksByIteration(Long iterationId);

    List<TaskResponse> getTasksByAssignee(Long assigneeId);

    List<TaskResponse> getMyTasks();

    TaskResponse getTaskById(Long id);

    TaskResponse createTask(TaskCreateRequest request);

    TaskResponse updateTask(Long id, TaskCreateRequest request);

    void deleteTask(Long id);

    void updateTaskStatus(Long id, Task.TaskStatus status);

    void updateTaskAssignee(Long id, Long assigneeId);
}
