package com.example.todolistcoursework.builder;

import com.example.todolistcoursework.model.dto.UserInfo;
import com.example.todolistcoursework.model.entity.User;

public class UserMapper {
    public static UserInfo toApi(User user) {
        return UserInfo.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
