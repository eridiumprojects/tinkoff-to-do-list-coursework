package com.example.todolistcoursework.model.enums;

import java.util.Map;

public enum TaskStatus {
    BACKLOG("backlog"),
    TODO("todo"),
    IN_PROGRESS("in progress"),
    DONE("done");
    public static final Map<TaskStatus, Integer> priorities = Map.of(
            TaskStatus.BACKLOG, 4,
            TaskStatus.TODO, 3,
            TaskStatus.IN_PROGRESS, 2,
            TaskStatus.DONE, 1);

    final String status;
    TaskStatus(String status) {
        this.status = status;
    }
}
