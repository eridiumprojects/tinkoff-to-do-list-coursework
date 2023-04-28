package com.example.todolistcoursework.model.dto.request;

import com.example.todolistcoursework.model.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskRequest {
    String data;
    LocalDateTime deadline;
    Long id;
    String description;
    TaskStatus status;
}
