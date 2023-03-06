package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.service.TaskService;
import com.example.todolistcoursework.util.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/search")
    public List<Task> searchTasks(
            @RequestParam(name = "data") Optional<String> data,
            @RequestParam(name = "checkbox") Optional<Boolean> checkbox,
            @RequestParam(defaultValue = "0", required = false, name = "actual") int actual,
            @RequestParam(name = "order_by") Optional<String> order
    ) {
        return taskService.searchTasks(data, checkbox, actual, order);
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
