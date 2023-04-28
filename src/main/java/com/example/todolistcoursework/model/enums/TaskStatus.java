package com.example.todolistcoursework.model.enums;

public enum TaskStatus {
    BACKLOG("backlog"),
    TODO("todo"),
    IN_PROGRESS("in progress"),
    DONE("done");

    final String status;
    TaskStatus(String status) {
        this.status = status;
    }
}
