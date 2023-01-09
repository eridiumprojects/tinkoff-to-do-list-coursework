package com.example.todolistcoursework.api.mappers;

import com.example.todolistcoursework.api.dtos.TaskDto;
import com.example.todolistcoursework.domain.entities.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    public Task toTask(TaskDto taskDto) {
        Task task = new Task();
        task.setData(taskDto.getData());
        task.setDeadline(taskDto.getDeadline());
        task.setCheckbox(false);
        return task;
    }
}
