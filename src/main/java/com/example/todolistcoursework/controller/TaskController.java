package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.request.CreateTaskRequest;
import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.request.UpdateTaskRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PostMapping("/create")
    public ResponseEntity<TaskInfo> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok
                (taskService.createTask(authService.getJwtAuth().getUserId(), TaskMapper.toModel(request)));
    }

    @Operation(
            summary = "Get task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task received successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<TaskInfo> getTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(authService.getJwtAuth().getUserId(), id));
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PutMapping("/update")
    public ResponseEntity<TaskInfo> updateTask(@Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(authService.getJwtAuth().getUserId(),
                TaskMapper.toModel(request)));
    }

    @Operation(
            summary = "Delete task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task deleted successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskInfo> deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(authService.getJwtAuth().getUserId(), id));
    }

    @Operation(
            summary = "Get list of tasks",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tasks retrieved successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/list")
    public ResponseEntity<List<TaskInfo>> getTasks(Integer page) {
        return ResponseEntity.ok(taskService.getTasks(authService.getJwtAuth().getUserId(), page));
    }

    @Operation(
            summary = "Filter tasks by criteria",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tasks filtered successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping(value = "/filter")
    public ResponseEntity<List<TaskInfo>> searchTasks(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(taskService.filterTasks(authService.getJwtAuth().getUserId(), filterRequest));
    }

    @GetMapping(value = "/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok(("Welcome back!"));
    }
}