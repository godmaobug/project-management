package com.projmaster.dto.request;

import com.projmaster.entity.Bug;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BugCreateRequest {

    @NotBlank(message = "Bug标题不能为空")
    private String title;

    private String description;

    private String reproduceSteps;

    @NotNull(message = "严重程度不能为空")
    private Bug.Severity severity;

    @NotNull(message = "环境不能为空")
    private Bug.Environment environment;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private Long requirementId;

    private Long assigneeId;

    private String version;

    private String module;
}
