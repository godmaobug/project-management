package com.projmaster.dto.response;

import com.projmaster.entity.Requirement;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequirementResponse {

    private Long id;
    private String title;
    private String description;
    private String acceptanceCriteria;
    private Requirement.Priority priority;
    private Requirement.RequirementStatus status;
    private Long projectId;
    private String projectName;
    private UserResponse creator;
    private UserResponse assignee;
    private Long iterationId;
    private String iterationName;
    private String source;
    private Integer storyPoints;
    private Double progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
