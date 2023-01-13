package com.example.todolistcoursework.domain.services;

import com.example.todolistcoursework.api.dtos.DeadlineDto;
import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.domain.entities.Task;
import com.example.todolistcoursework.domain.repositories.TaskRepository;
import com.example.todolistcoursework.exceptions.ObjectNotFoundException;
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
        return taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
        task.setData(taskDto.getData());
        task.setDeadline(taskDto.getDeadline());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!")));
    }

    public Task taskComplete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
        task.setCheckbox(true);
        return taskRepository.save(task);
    }

    public Task setDeadline(Long id, DeadlineDto deadline) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
        task.setDeadline(deadline.getDeadline());
        return taskRepository.save(task);
    }
}
