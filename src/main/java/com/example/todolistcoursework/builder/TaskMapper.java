package com.example.todolistcoursework.builder;

import com.example.todolistcoursework.model.dto.request.CreateTaskRequest;
import com.example.todolistcoursework.model.dto.request.UpdateTaskRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    public static Task toModel(CreateTaskRequest request) {
        return Task.builder()
                .data(request.getData())
                .created(LocalDateTime.now())
                .checkbox(false)
                .modified(LocalDateTime.now())
                .build();
    }

    public static Task toModel(UpdateTaskRequest request) {
        return Task.builder()
                .data(request.getData())
                .checkbox(false)
                .id(request.getId())
                .build();
    }

    public static TaskInfo toApi(Task task) {
        return TaskInfo.builder()
                .deadline(task.getDeadline())
                .checkbox(task.isCheckbox())
                .id(task.getId())
                .data(task.getData())
                .build();
    }
}
