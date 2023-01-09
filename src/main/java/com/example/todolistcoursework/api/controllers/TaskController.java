package com.example.todolistcoursework.api.controllers;

import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.api.mappers.TaskMapper;
import com.example.todolistcoursework.domain.entities.Task;
import com.example.todolistcoursework.domain.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("")
//g
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping(value = "create", consumes = "application/json")
    public Task createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskMapper.toTask(taskDto));
    }

    @GetMapping(value = "task/{id}", produces = "application/json")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping(value = "update/{id}", consumes = "application/json")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id,taskDto);
    }

    @DeleteMapping(value = "delete/{id}",consumes = "application/json")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping(value = "tasks",produces = "application/json")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }


}
