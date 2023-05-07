package com.example.todolistcoursework.model.dto.request;

import com.example.todolistcoursework.model.enums.SortOrder;
import com.example.todolistcoursework.model.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    List<SortOrder> orders;
    List<TaskStatus> necessaryStatuses;
    Integer page;
}
