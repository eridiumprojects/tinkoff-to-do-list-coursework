package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.enums.SortOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskFilter {
    public static List<TaskInfo> filter(FilterRequest filterRequest, List<Task> tasks) {
        if (filterRequest.getOrders() != null)
            for (var el : filterRequest.getOrders())
                tasks = filterInternal(tasks, el);

        if (filterRequest.getFromDate() != null)
            tasks = tasks.stream().filter(e -> e.getDeadline().isAfter(filterRequest.getFromDate())).toList();

        if (filterRequest.getToDate() != null)
            tasks = tasks.stream().filter(e -> e.getDeadline().isBefore(filterRequest.getToDate())).toList();

        return tasks.stream().map(TaskMapper::toApi).toList();
    }

    private static List<Task> filterInternal(List<Task> tasks, SortOrder order) {
        switch (order) {
            case ALPHABET -> {
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getData)).toList();
            }
            case DEADLINE -> {
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getDeadline,
                                Comparator.nullsLast(Comparator.naturalOrder()))).toList();
            }
            case COMPLETED -> {
                return tasks.stream().sorted(Comparator.comparing(Task::isCheckbox)).toList();
            }
            case ALPHABET_DESC -> {
                return tasks
                        .stream()
                        .sorted(Comparator.comparing(Task::getData).reversed()).toList();
            }
            case DEADLINE_DESC -> {
                return tasks
                        .stream()
                        .sorted(Comparator
                                .comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder()))
                                .reversed())
                        .toList();
            }
            case COMPLETED_DESC -> {
                return tasks.stream().sorted(Comparator.comparing(Task::isCheckbox).reversed()).toList();
            }
        }
        return new ArrayList<>();
    }
}