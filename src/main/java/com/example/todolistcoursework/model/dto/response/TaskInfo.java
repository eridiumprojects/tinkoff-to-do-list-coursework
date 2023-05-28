package com.example.todolistcoursework.model.dto.response;

import com.example.todolistcoursework.model.enums.TaskStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {
    Long id;
    String data;
    LocalDateTime deadline;
    TaskStatus status;
    String description;
}
