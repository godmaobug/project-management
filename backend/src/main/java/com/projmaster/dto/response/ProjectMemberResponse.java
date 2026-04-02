package com.projmaster.dto.response;

import com.projmaster.entity.ProjectMember;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectMemberResponse {

    private Long id;
    private UserResponse user;
    private ProjectMember.ProjectRole role;
    private LocalDateTime joinedAt;
}
