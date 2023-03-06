package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import com.example.todolistcoursework.repository.RoleRepository;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import com.example.todolistcoursework.security.UserDetailsImpl;
import com.example.todolistcoursework.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    private User getUser() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("There is no such user");
        }
        return user.get();
    }

    public Task createTask(Task task) {
        User user = getUser();
        task.setUser(user);
        taskRepository.save(task);
        return task;
    }

    public Task getTask(Long id) {
        User user = getUser();
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().equals(user)) {
            return task.get();
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public List<Task> getTasks() {
        User user = getUser();
        return user.getTasks().stream().toList();
    }

    public Task updateTask(Long id, TaskDto taskDto) {
        User user = getUser();
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().equals(user)) {
            Task existingTask = task.get();
            existingTask.setData(taskDto.getData());
            existingTask.setModified(LocalDateTime.now());
            taskRepository.save(existingTask);
            return existingTask;
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public void deleteTask(Long id) {
        User user = getUser();
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().equals(user)) {
            taskRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public Task tickTask(Long id) {
        User user = getUser();
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().equals(user)) {
            Task existingTask = task.get();
            existingTask.tick();
            taskRepository.save(existingTask);
            return existingTask;
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public List<Task> searchTasks(
            Optional<String> data, Optional<Boolean> checkbox, int actual, Optional<String> order
    ) {
        if (order.isEmpty()) {
            if (data.isPresent()) {
                return taskRepository.findAllByData(data.get());
            }
            if (checkbox.isPresent()) {
                return taskRepository.findAllByCheckbox(checkbox.get());
            }
        } else {
            if (data.isPresent() && order.get().equals("created_time")) {
                return taskRepository.findAllByDataOrderQuery(data.get());
            }
            if (checkbox.isPresent() && order.get().equals("created_time")) {
                return taskRepository.findAllByCheckboxOrderQuery(checkbox.get());
            }
        }
        if (actual == 1) {
            return taskRepository.findAllByOrderByCreated();
        } else {
            return taskRepository.findAllCompletedTasks();
        }
    }

    public List<Task> getActualTasks() {
        User user = getUser();
        return user.getTasks().stream().filter(a -> !a.isCheckbox()).toList();
    }

    public List<Task> getCompletedTasks() {
        User user = getUser();
        return user.getTasks().stream().filter(a -> !a.isCheckbox()).toList();
    }
}
