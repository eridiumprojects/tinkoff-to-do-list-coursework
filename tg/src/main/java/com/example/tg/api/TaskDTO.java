package com.example.tg.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskDTO implements Serializable {
    @JsonIgnore
    private Long id;
    @NotEmpty
    private String data;
    @JsonIgnore
    private LocalDateTime deadline;
    @JsonIgnore
    private boolean checkbox;
}
