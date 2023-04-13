package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.request.CreateTaskRequest;
import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.request.UpdateTaskRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.TaskService;
import com.example.todolistcoursework.builder.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TaskInfo> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(authService.getJwtAuth().getUserId(), TaskMapper.toModel(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskInfo> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(authService.getJwtAuth().getUserId(), id));
    }

    @PutMapping("/update")
    public ResponseEntity<TaskInfo> updateTask(@Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(authService.getJwtAuth().getUserId(), TaskMapper.toModel(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskInfo> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(authService.getJwtAuth().getUserId(), id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TaskInfo>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks(authService.getJwtAuth().getUserId()));
    }

    @PutMapping(value = "/tick/{id}")
    public ResponseEntity<TaskInfo> tickTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.tickTask(authService.getJwtAuth().getUserId(), id));
    }

    @GetMapping(value = "/filter")
    public ResponseEntity<List<TaskInfo>> searchTasks(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(taskService.filterTasks(authService.getJwtAuth().getUserId(), filterRequest));
    }

    @GetMapping(value = "/actual")
    public ResponseEntity<List<TaskInfo>> getActualTasks() {
        return ResponseEntity.ok(taskService.getActualTasks(authService.getJwtAuth().getUserId()));
    }

    @GetMapping(value = "/completed")
    public ResponseEntity<List<TaskInfo>> getCompletedTasks() {
        return ResponseEntity.ok(taskService.getCompletedTasks(authService.getJwtAuth().getUserId()));
    }
}