package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.FilterRequest;
import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("There is no such user");
        }
        return user.get();
    }

    public Task createTask(Long userId, Task task) {
        User user = getUser(userId);
        task.setUser(user);
        taskRepository.save(task);
        return task;
    }

    public Task getTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            return task.get();
        } else {
            throw new ObjectNotFoundException("This user does not have such task");
        }
    }

    public List<Task> getTasks(Long userId) {
        User user = getUser(userId);
        return user.getTasks().stream().toList();
    }

    public Task updateTask(Long userId, TaskDto taskDto) {
        Optional<Task> task = taskRepository.findById(taskDto.getId());
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            Task existingTask = task.get();
            existingTask.setData(taskDto.getData());
            existingTask.setModified(LocalDateTime.now());
            taskRepository.save(existingTask);
            return existingTask;
        } else {
            throw new ObjectNotFoundException("This user does not have such task");
        }
    }

    public void deleteTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            taskRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("This user does not have such task");
        }
    }

    public Task tickTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            Task existingTask = task.get();
            existingTask.tick();
            taskRepository.save(existingTask);
            return existingTask;
        } else {
            throw new ObjectNotFoundException("This user does not have such task");
        }
    }

    public List<Task> filterTasks(Long userId, FilterRequest filterRequest) {
        return TaskFilter.filter(filterRequest, getUser(userId).getTasks().stream().toList());
    }

    public List<Task> getActualTasks(Long userId) {
        return getUser(userId).getTasks().stream().filter(a -> !a.isCheckbox()).toList();
    }

    public List<Task> getCompletedTasks(Long userId) {
        return getUser(userId).getTasks().stream().filter(Task::isCheckbox).toList();
    }
}