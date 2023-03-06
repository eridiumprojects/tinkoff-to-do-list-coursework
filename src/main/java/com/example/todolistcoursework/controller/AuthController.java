package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.LoginRequest;
import com.example.todolistcoursework.model.dto.RefreshRequest;
import com.example.todolistcoursework.model.dto.SignupRequest;
import com.example.todolistcoursework.service.RefreshTokenService;
import com.example.todolistcoursework.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public AuthController(UserService userService, RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;

    }
    UserService userService;
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshRequest request) {
        return refreshTokenService.refresh(request);
    }
}

