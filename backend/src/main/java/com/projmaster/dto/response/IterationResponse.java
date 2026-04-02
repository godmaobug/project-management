package com.projmaster.dto.response;

import com.projmaster.entity.Iteration;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IterationResponse {

    private Long id;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectId;
    private String projectName;
    private Iteration.IterationStatus status;
    private Double totalStoryPoints;
    private Double completedStoryPoints;
    private Double progress;
    private Integer totalTasks;
    private Integer completedTasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
