package com.example.telegram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {
    public final TaskService taskService;
    public final AuthService authService;
}
