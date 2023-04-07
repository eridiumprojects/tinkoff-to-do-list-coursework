package com.example.tg.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginDTO {
    private String username;
    private String password;
    @JsonIgnore
    private UUID deviceToken;
}
