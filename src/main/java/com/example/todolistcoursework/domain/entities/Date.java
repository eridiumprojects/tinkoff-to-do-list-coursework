package com.example.todolistcoursework.domain.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Date {
    private String day;
    private String month;
    private int year;
    private String hours;
    private String seconds;
}
