package com.example.tg.api;

import lombok.Data;

@Data
public class LoginDTO {
    private long chatId;
    private String username;
    private String password;
}
