package com.example.todolistcoursework.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SignupRequest {
    @NotEmpty String username;
    @NotEmpty String firstName;
    @NotEmpty String lastName;
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+") String email;
    @NotEmpty String password;
}
