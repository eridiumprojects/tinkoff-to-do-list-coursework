package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.response.UserInfoResponse;
import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Get current user information")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User information received successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))
                    ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username doesn't exists",
                    content = @Content(schema = @Schema(implementation = AuthException.class))
            )
    })
    @GetMapping("/current")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getUserInfo(authService.getJwtAuth().getUserId()));
    }

    @Operation(summary = "Delete current user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "User doesn't exists")
    })
    @DeleteMapping("/current")
    public ResponseEntity<UserInfoResponse> deleteCurrentUser() {
        return ResponseEntity.ok(userService.deleteUser(authService.getJwtAuth().getUserId()));
    }
}
