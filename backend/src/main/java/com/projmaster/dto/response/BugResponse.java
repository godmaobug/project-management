package com.projmaster.dto.response;

import com.projmaster.entity.Bug;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BugResponse {

    private Long id;
    private String title;
    private String description;
    private String reproduceSteps;
    private Bug.Severity severity;
    private Bug.BugStatus status;
    private Bug.Environment environment;
    private Long projectId;
    private String projectName;
    private Long requirementId;
    private String requirementTitle;
    private UserResponse reporter;
    private UserResponse assignee;
    private String version;
    private String module;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
}
