package com.example.todolistcoursework.api.mappers;

import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.domain.entities.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    public Task toTask(TaskDto taskDto) {
        Task task = new Task();
        task.setData(taskDto.getData());
        task.setDeadline(null);
        task.setModified(null);
        task.setCheckbox(false);
        LocalDateTime created = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedCreated = created.format(myFormatObj);
        task.setCreated(formattedCreated);
        return task;
    }
}
