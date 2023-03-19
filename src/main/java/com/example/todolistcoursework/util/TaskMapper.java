package com.example.todolistcoursework.util;

import com.example.todolistcoursework.model.dto.TaskDto;
import com.example.todolistcoursework.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    public Task toTask(TaskDto taskDto) {
        Task task = new Task();
        task.setData(taskDto.getData());
        task.setCreated(LocalDateTime.now());
        task.setDeadline(null);
        task.setModified(null);
        task.setCheckbox(taskDto.isCheckbox());
        return task;
    }
}
