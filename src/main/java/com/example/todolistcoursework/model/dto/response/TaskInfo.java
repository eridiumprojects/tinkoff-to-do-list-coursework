package com.example.todolistcoursework.model.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskInfo {
    Long id;
    String data;
    LocalDateTime deadline;
    Boolean checkbox;
}
