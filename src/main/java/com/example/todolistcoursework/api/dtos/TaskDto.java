package com.example.todolistcoursework.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TaskDto {
    private String data;
    @JsonIgnore
    private boolean checkbox;
    private String deadline;

}
