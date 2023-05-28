package com.example.todolistcoursework.service;

import com.example.todolistcoursework.model.dto.request.FilterRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.enums.SortOrder;
import com.example.todolistcoursework.model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TaskFilterTest {
    public List<Task> createListOfTasksByDefault() {
        return List.of(
                Task.builder()
                        .id(1L)
                        .description("Join in Tinkoff")
                        .data("Pass an interview")
                        .user(null)
                        .build(),
                Task.builder()
                        .id(2L)
                        .description("Breakfast")
                        .data("Take a meal")
                        .user(null)
                        .build()
        );
    }

    @Test
    void filter() {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setOrders(List.of(SortOrder.DEADLINE,
                SortOrder.ALPHABET));
        filterRequest.setPage(1);
        filterRequest.setNecessaryStatuses(List.of(TaskStatus.BACKLOG,TaskStatus.DONE));
        List<TaskInfo> list = TaskFilter.filter(filterRequest, createListOfTasksByDefault());

        assertEquals(createListOfTasksByDefault().size(), list.size());
    }
}