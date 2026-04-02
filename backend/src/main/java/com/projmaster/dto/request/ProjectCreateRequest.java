package com.projmaster.dto.request;

import com.projmaster.entity.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateRequest {

    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "项目类型不能为空")
    private Project.ProjectType type;

    private LocalDate startDate;

    private LocalDate endDate;

    private Project.Visibility visibility = Project.Visibility.PRIVATE;
}
