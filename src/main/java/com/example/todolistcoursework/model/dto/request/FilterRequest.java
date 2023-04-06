package com.example.todolistcoursework.model.dto.request;

import com.example.todolistcoursework.model.enums.SortOrder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    List<SortOrder> orders;
    LocalDateTime fromDate;
    LocalDateTime toDate;

}
