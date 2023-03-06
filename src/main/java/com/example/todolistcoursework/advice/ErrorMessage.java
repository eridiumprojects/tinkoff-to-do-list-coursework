package com.example.todolistcoursework.advice;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}
