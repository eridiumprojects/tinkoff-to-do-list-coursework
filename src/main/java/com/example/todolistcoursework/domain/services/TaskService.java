package com.example.todolistcoursework.domain.services;

import com.example.todolistcoursework.api.dtos.DeadlineDto;
import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.domain.entities.Date;
import com.example.todolistcoursework.domain.entities.Task;
import com.example.todolistcoursework.domain.repositories.TaskRepository;
import com.example.todolistcoursework.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        LocalDateTime modified = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedModified = modified.format(myFormatObj);
        task.setModified(formattedModified);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!")));
    }

    public Task taskComplete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
        task.setCheckbox(true);
        LocalDateTime modified = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedModified = modified.format(myFormatObj);
        task.setModified(formattedModified);
        return taskRepository.save(task);
    }

    public Task setDeadline(Long id, DeadlineDto deadline) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("There was no task with that ID!"));
        Date date = new Date();
        if (deadline.getDay().length() == 1) {
            deadline.setDay("0" + deadline.getDay());
        }
        if (deadline.getMonth().length() == 1) {
            deadline.setMonth("0" + deadline.getMonth());
        }
        if (Integer.parseInt(deadline.getMonth()) > 12 ||
                Integer.parseInt(deadline.getDay()) > 31 ||
                Integer.parseInt(deadline.getHours()) > 24 ||
                Integer.parseInt(deadline.getSeconds()) > 60) {
            throw new RuntimeException("Unreachable statement!");
        }
        date.setDay(deadline.getDay());
        date.setMonth(deadline.getMonth());
        date.setYear(deadline.getYear());
        date.setHours(deadline.getHours());
        date.setSeconds(deadline.getSeconds());
        String formattedDeadline = date.getDay() + "-" + date.getMonth() + "-" + date.getYear() + "_"
                + date.getHours() + ":" + date.getSeconds();
        task.setDeadline(formattedDeadline);

        LocalDateTime modified = LocalDateTime.now();
        DateTimeFormatter obj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedModified = modified.format(obj);
        task.setModified(formattedModified);
        return taskRepository.save(task);
    }

    public List<Task> searchTasks(Optional<String> data, Optional<Boolean> checkbox,
                                  int actual, Optional<String> order) {
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
        return taskRepository.findAllByOrderByCreated();
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.findAllCompletedTasks();
    }
}
