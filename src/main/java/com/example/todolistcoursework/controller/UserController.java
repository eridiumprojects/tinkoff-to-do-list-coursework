package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.UserInfo;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<UserInfo> getCurrentUser() {
        return ResponseEntity.ok(userService.getUserInfo(authService.getJwtAuth().getUserId()));
    }

    @DeleteMapping("/current")
    public ResponseEntity<UserInfo> deleteCurrentUser() {
        return ResponseEntity.ok(userService.deleteUser(authService.getJwtAuth().getUserId()));
    }
}
