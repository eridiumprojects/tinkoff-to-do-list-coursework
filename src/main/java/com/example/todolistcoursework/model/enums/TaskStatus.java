package com.example.todolistcoursework.model.enums;

import java.util.Map;

public enum TaskStatus {
    BACKLOG("backlog"),
    TODO("todo"),
    IN_PROGRESS("in progress"),
    DONE("done");
    private static final Map<TaskStatus, Integer> priorities = Map.of(
            TaskStatus.BACKLOG, 3,
            TaskStatus.TODO, 2,
            TaskStatus.IN_PROGRESS, 1,
            TaskStatus.DONE, 4);

    final String status;
    TaskStatus(String status) {
        this.status = status;
    }

    public static int getPriority(TaskStatus taskStatus) {
        return priorities.get(taskStatus);
    }
}
