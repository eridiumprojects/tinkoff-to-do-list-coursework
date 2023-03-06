package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.FilterRequest;
import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import com.example.todolistcoursework.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

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
        //аналогично с нижним
        if (task.isPresent() && task.get().getUser().equals(user)) {
            taskRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public Task tickTask(Long id) {
        User user = getUser();
        Optional<Task> task = taskRepository.findById(id);
        //тут легче сделать в if отрицание, а ниже уже просто закинуть все без else
        if (task.isPresent() && task.get().getUser().equals(user)) {
            Task existingTask = task.get();
            existingTask.tick();
            taskRepository.save(existingTask);
            return existingTask;
        } else {
            throw new ObjectNotFoundException("There was no task with that ID!");
        }
    }

    public List<Task> filterTasks(FilterRequest filterRequest) {
        return TaskFilter.filter(filterRequest, getUser().getTasks().stream().toList());
    }

    public List<Task> getActualTasks() {
        User user = getUser();
        return user.getTasks().stream().filter(a -> !a.isCheckbox()).toList();
    }

    public List<Task> getCompletedTasks() {
        User user = getUser();
        return user.getTasks().stream().filter(Task::isCheckbox).toList();
    }
}
