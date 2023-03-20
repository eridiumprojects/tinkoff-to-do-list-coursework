package com.example.todolistcoursework.model.dto;

import com.example.todolistcoursework.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserInfo {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<Role> roles;
}
