package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.FilterRequest;
import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.service.TaskService;
import com.example.todolistcoursework.util.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping(value = "/create")
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.createTask(taskMapper.toTask(taskDto));
    }

    @GetMapping(value = "/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping(value = "/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PutMapping(value = "/tick/{id}")
    public Task tickTask(@PathVariable Long id) {
        return taskService.tickTask(id);
    }

    @GetMapping(value = "/filter")
    public List<Task> searchTasks(@RequestBody FilterRequest filterRequest) {
        return taskService.filterTasks(filterRequest);
    }

    @GetMapping(value = "/actual")
    public List<Task> getActualTasks() {
        return taskService.getActualTasks();
    }

    @GetMapping(value = "/completed")
    public List<Task> getCompletedTasks() {
        return taskService.getCompletedTasks();
    }

}
