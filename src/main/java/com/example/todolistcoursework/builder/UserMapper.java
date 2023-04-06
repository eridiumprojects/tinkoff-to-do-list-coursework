package com.example.todolistcoursework.builder;

import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.model.entity.User;

public class UserMapper {
    public static UserInfoResponse toApi(User user) {
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
