package com.projmaster.dto.response;

import com.projmaster.entity.Task;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Task.TaskType type;
    private Task.TaskStatus status;
    private Task.Priority priority;
    private Long projectId;
    private String projectName;
    private Long requirementId;
    private String requirementTitle;
    private Long iterationId;
    private String iterationName;
    private UserResponse assignee;
    private UserResponse creator;
    private Long parentId;
    private List<TaskResponse> subtasks;
    private Double estimatedHours;
    private Double actualHours;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long dependsOnId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
