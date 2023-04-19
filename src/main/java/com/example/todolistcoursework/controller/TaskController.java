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
                    description = "Task updated successfully"),
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
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<List<TaskInfo>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks(authService.getJwtAuth().getUserId()));
    }

    @Operation(
            summary = "Tick a task by ID",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task ticked successfully",
                    content = @Content(schema = @Schema(implementation = TaskInfo.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found")
    })
    @PutMapping(value = "/tick/{id}")
    public ResponseEntity<TaskInfo> tickTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.tickTask(authService.getJwtAuth().getUserId(), id));
    }

    @Operation(
            summary = "Filter tasks by criteria",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks filtered successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping(value = "/filter")
    public ResponseEntity<List<TaskInfo>> searchTasks(@Valid @RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(taskService.filterTasks(authService.getJwtAuth().getUserId(), filterRequest));
    }

    @Operation(
            summary = "Get list of actual tasks",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actual tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping(value = "/actual")
    public ResponseEntity<List<TaskInfo>> getActualTasks() {
        return ResponseEntity.ok(taskService.getActualTasks(authService.getJwtAuth().getUserId()));
    }

    @Operation(
            summary = "Get list of completed tasks",
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Completed tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping(value = "/completed")
    public ResponseEntity<List<TaskInfo>> getCompletedTasks() {
        return ResponseEntity.ok(taskService.getCompletedTasks(authService.getJwtAuth().getUserId()));
    }
}