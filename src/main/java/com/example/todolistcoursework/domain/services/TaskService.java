package com.example.todolistcoursework.domain.services;

import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.domain.entities.Task;
import com.example.todolistcoursework.domain.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
        task.setData(taskDto.getData());
        task.setDeadline(taskDto.getDeadline());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new RuntimeException()));
    }
}
