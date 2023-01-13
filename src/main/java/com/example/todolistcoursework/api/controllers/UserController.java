package com.example.todolistcoursework.api.controllers;

import com.example.todolistcoursework.api.dtos.UserDto;
import com.example.todolistcoursework.api.mappers.UserMapper;
import com.example.todolistcoursework.domain.entities.User;
import com.example.todolistcoursework.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "register", consumes = "application/json")
    public User register(@Valid @RequestBody UserDto userDto) {
        return userService.register(userMapper.toUser(userDto));
    }

}
