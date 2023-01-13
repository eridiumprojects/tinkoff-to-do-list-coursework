package com.example.todolistcoursework.api.controllers;

import com.example.todolistcoursework.api.dtos.DeadlineDto;
import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.api.mappers.TaskMapper;
import com.example.todolistcoursework.domain.entities.Task;
import com.example.todolistcoursework.domain.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping(value = "create", consumes = "application/json")
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.createTask(taskMapper.toTask(taskDto));
    }

    @GetMapping(value = "task/{id}", produces = "application/json")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping(value = "update/{id}", consumes = "application/json")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping(value = "delete/{id}", produces = "application/json")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping(value = "tasks", produces = "application/json")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PutMapping(value = "task/notice/{id}", produces = "application/json")
    public Task taskCompleted(@PathVariable Long id) {
        return taskService.taskComplete(id);
    }

    @PutMapping(value = "task/deadline/{id}", produces = "application/json")
    public Task setTaskDeadline(@PathVariable Long id, @Valid @RequestBody DeadlineDto deadline) {
        return taskService.setDeadline(id, deadline);
    }
}
