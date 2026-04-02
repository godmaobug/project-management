package com.projmaster.dto.request;

import com.projmaster.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "任务类型不能为空")
    private Task.TaskType type;

    private Task.Priority priority = Task.Priority.MEDIUM;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private Long requirementId;

    private Long iterationId;

    private Long assigneeId;

    private Long parentId;

    private Double estimatedHours;

    private LocalDate startDate;

    private LocalDate dueDate;

    private Long dependsOnId;
}
