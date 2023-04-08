package com.example.telegram.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginRequest {
    private String username;
    private String password;
    @JsonIgnore
    private UUID deviceToken;
}
