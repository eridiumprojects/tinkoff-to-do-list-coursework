package com.example.todolistcoursework.api.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DeadlineDto {
    private String day;
    private String month;
    private int year;
    private String hours;
    private String seconds;
}
