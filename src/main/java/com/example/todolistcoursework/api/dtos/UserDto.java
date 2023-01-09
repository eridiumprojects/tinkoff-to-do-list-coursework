package com.example.todolistcoursework.api.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
public class UserDto {
    private String login;
    private String password;
    private String email;
}
