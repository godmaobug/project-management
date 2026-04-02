package com.projmaster.dto.response;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String realName;
    private String avatar;
    private String role;

    public JwtResponse(String token, Long id, String username, String realName, String avatar, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.avatar = avatar;
        this.role = role;
    }
}
