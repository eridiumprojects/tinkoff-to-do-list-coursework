package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.FilterRequest;
import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.TaskService;
import com.example.todolistcoursework.builder.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final AuthService authService;

    @PostMapping("/create")
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.createTask(authService.getJwtAuth().getUserId(), TaskMapper.toApi(taskDto));
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(authService.getJwtAuth().getUserId(), id);
    }

    @PutMapping("/update")
    public Task updateTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.updateTask(authService.getJwtAuth().getUserId(), taskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(authService.getJwtAuth().getUserId(), id);
    }

    @GetMapping("/list")
    public List<Task> getTasks() {
        return taskService.getTasks(authService.getJwtAuth().getUserId());
    }

    @PutMapping(value = "/tick/{id}")
    public Task tickTask(@PathVariable Long id) {
        return taskService.tickTask(authService.getJwtAuth().getUserId(), id);
    }

    @GetMapping(value = "/filter")
    public List<Task> searchTasks(@RequestBody FilterRequest filterRequest) {
        return taskService.filterTasks(authService.getJwtAuth().getUserId(), filterRequest);
    }

    @GetMapping(value = "/actual")
    public List<Task> getActualTasks() {
        return taskService.getActualTasks(authService.getJwtAuth().getUserId());
    }

    @GetMapping(value = "/completed")
    public List<Task> getCompletedTasks() {
        return taskService.getCompletedTasks(authService.getJwtAuth().getUserId());
    }
}
