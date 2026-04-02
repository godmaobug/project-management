package com.projmaster.dto.response;

import com.projmaster.entity.Project;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Project.ProjectType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserResponse owner;
    private Project.ProjectStatus status;
    private Project.Visibility visibility;
    private Boolean archived;
    private Double progress;
    private LocalDateTime createdAt;
    private List<ProjectMemberResponse> members;
}
