package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.enums.TaskStatus;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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
            throw new ObjectNotFoundException("Error: There is no such user");
        }
        return user.get();
    }

    public TaskInfo createTask(Long userId, Task task) {
        User user = getUser(userId);
        task.setUser(user);
        task.setCreated(task.getCreated());
        taskRepository.save(task);
        return TaskMapper.toApi(task);
    }

    public TaskInfo getTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            return TaskMapper.toApi(task.get());
        } else {
            throw new ObjectNotFoundException("Error: This user does not have such task");
        }
    }

    public List<TaskInfo> getTasks(Long userId) {
        User user = getUser(userId);
        return user.getTasks().stream()
                .sorted(Comparator.comparing(Task::getCreated))
                .sorted(Comparator.comparing(Task::getStatus))
                .map(TaskMapper::toApi).toList();
    }

    public TaskInfo updateTask(Long userId, Task request) {
        Optional<Task> task = taskRepository.findById(request.getId());
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            Task existingTask = task.get();
            existingTask.setData(request.getData());
            existingTask.setModified(LocalDateTime.now());
            existingTask.setDescription(request.getDescription());
            existingTask.setStatus(request.getStatus());
            var result = taskRepository.save(existingTask);
            return TaskMapper.toApi(result);
        } else {
            throw new ObjectNotFoundException("Error: This user does not have such task");
        }
    }

    public TaskInfo deleteTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            taskRepository.deleteById(id);
            return TaskMapper.toApi(task.get());
        } else {
            throw new ObjectNotFoundException("Error: This user does not have such task");
        }
    }

    public List<TaskInfo> filterTasks(Long userId, FilterRequest filterRequest) {
        var user = getUser(userId);
        Hibernate.initialize(user.getTasks());
        return TaskFilter.filter(filterRequest, user.getTasks().stream().toList());
    }

    public List<TaskInfo> getActualTasks(Long userId) {
        var user = getUser(userId);
        Hibernate.initialize(user.getTasks());
        return user.getTasks().stream()
                .filter(a -> a.getStatus().equals(TaskStatus.IN_PROGRESS) || a.getStatus().equals(TaskStatus.TODO))
                .map(TaskMapper::toApi)
                .toList();
    }

    public List<TaskInfo> getCompletedTasks(Long userId) {
        var user = getUser(userId);
        Hibernate.initialize(user.getTasks());
        return user.getTasks().stream()
                .filter(a -> a.getStatus().equals(TaskStatus.DONE))
                .map(TaskMapper::toApi)
                .toList();
    }
}
