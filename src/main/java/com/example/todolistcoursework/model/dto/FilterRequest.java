package com.example.todolistcoursework.model.dto;

import com.example.todolistcoursework.model.enums.SortOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FilterRequest {
    List<SortOrder> orders;
    LocalDateTime fromDate;
    LocalDateTime toDate;

}
