package com.projmaster.dto.response;

import com.projmaster.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private User.UserRole role;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
