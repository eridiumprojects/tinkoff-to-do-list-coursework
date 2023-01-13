package com.example.todolistcoursework.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TaskDto {
    @NotEmpty
    private String data;
    @JsonIgnore
    private boolean checkbox;
    @JsonIgnore
    private String deadline;

}
