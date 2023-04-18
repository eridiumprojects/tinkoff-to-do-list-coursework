package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Get current user information", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User information retrieved successfully")
    })
    @GetMapping("/current")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getUserInfo(authService.getJwtAuth().getUserId()));
    }

    @Operation(summary = "Delete current user", responses = {
            @ApiResponse(responseCode = "200",
                    description = "User deleted successfully")
    })
    @DeleteMapping("/current")
    public ResponseEntity<UserInfoResponse> deleteCurrentUser() {
        return ResponseEntity.ok(userService.deleteUser(authService.getJwtAuth().getUserId()));
    }
}
