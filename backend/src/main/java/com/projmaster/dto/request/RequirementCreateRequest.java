package com.projmaster.dto.request;

import com.projmaster.entity.Requirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequirementCreateRequest {

    @NotBlank(message = "需求标题不能为空")
    private String title;

    private String description;

    private String acceptanceCriteria;

    @NotNull(message = "优先级不能为空")
    private Requirement.Priority priority;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private Long assigneeId;

    private String source;

    private Integer storyPoints;
}
