package com.example.todolistcoursework.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(
            String accessToken,
            String refreshToken,
            Long id,
            String username,
            String email,
            List<String> roles
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}