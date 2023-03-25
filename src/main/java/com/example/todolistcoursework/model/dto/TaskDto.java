package com.example.todolistcoursework.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskDto {
    @JsonIgnore
    private Long id;
    @NotEmpty
    private String data;
    @JsonIgnore
    private LocalDateTime deadline;
    @JsonIgnore
    private boolean checkbox;
}
