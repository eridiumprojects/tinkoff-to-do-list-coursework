package com.example.todolistcoursework.advice;

import lombok.Getter;

import java.util.Date;

@Getter
public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}
