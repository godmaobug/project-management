package com.projmaster.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IterationCreateRequest {

    @NotBlank(message = "迭代名称不能为空")
    private String name;

    private String goal;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;
}
