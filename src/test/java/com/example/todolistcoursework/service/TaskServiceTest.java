package com.example.todolistcoursework.service;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.repository.TaskRepository;
import com.example.todolistcoursework.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "anton", roles = "USER")
class TaskServiceTest {
    @Mock
    public TaskRepository taskRepository;
    @Mock
    public UserRepository userRepository;
    @Mock
    public PageRequest pageRequest;
    @InjectMocks
    public TaskService taskService;

    public Task createTaskByDefault() {
        return Task.builder()
                .id(1L)
                .description("Join in Tinkoff")
                .data("Pass an interview")
                .user(createUserByDefault())
                .build();
    }

    public User createUserByDefault() {
        return User.builder()
                .id(1L)
                .firstName("Anton")
                .lastName("Pestrikov")
                .username("eridium")
                .email("pestrikov@mail.ru")
                .password("pestrikov123")
                .devices(List.of())
                .build();
    }

    public TaskInfo createTaskInfoByDefault() {
        return TaskMapper.toApi(createTaskByDefault());
    }

    public List<Task> createListOfTasksByDefault() {
        return List.of(
                Task.builder()
                        .id(1L)
                        .description("Join in Tinkoff")
                        .data("Pass an interview")
                        .user(createUserByDefault())
                        .build(),
                Task.builder()
                        .id(2L)
                        .description("Breakfast")
                        .data("Take a meal")
                        .user(createUserByDefault())
                        .build()
        );
    }

    public List<TaskInfo> createListOfTasksInfoByDefault() {
        return createListOfTasksByDefault()
                .stream()
                .map(TaskMapper::toApi)
                .collect(Collectors.toList());
    }

    @Test
    void createTaskSuccess() {
        //given
        User user = createUserByDefault();
        Task actualTask = createTaskByDefault();
        TaskInfo expectedTask = createTaskInfoByDefault();
        //when
        when(taskRepository.save(any(Task.class))).thenReturn(actualTask);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        TaskInfo afterResponse = taskService.createTask(user.getId(), actualTask);
        //then
        assertNotNull(afterResponse);
        assertEquals(expectedTask,afterResponse);
    }

    @Test
    void getTaskSuccess() {
        //given
        Task actualTask = createTaskByDefault();
        TaskInfo expectedTask = createTaskInfoByDefault();
        //when
        when(taskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(actualTask));
        TaskInfo afterResponse = taskService.getTask(1L, 1L);
        //then
        assertNotNull(afterResponse);
        assertEquals(expectedTask, afterResponse);
    }

    @Test
    void updateTaskSuccess() {
        //given
        Task actualTask = createTaskByDefault();
        Task updatedTask = createTaskByDefault();
        updatedTask.setData("new data");
        TaskInfo oldInfo = createTaskInfoByDefault();
        //when
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(actualTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        TaskInfo newInfo = taskService.updateTask(1L, actualTask);
        //then
        assertNotEquals(oldInfo,newInfo);
        assertNotNull(newInfo);
    }

//    @Test
//    void getTasksSuccess() {
//        int page = 1;
//        int pageSize = 5;
//        List<Task> tasks = createListOfTasksByDefault();
//        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("created").descending());
//
//        when(taskRepository.findTasksByUserId(1L, pageRequest)).thenReturn(any());
//        assertEquals(tasks, taskService.getTasks(1L, page));
//    }

    @Test
    void deleteTaskSuccess() {
        //given
        Task actualTask = createTaskByDefault();
        //when
        when(taskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(actualTask));
        taskService.deleteTask(1L, 1L);
        //then
        verify(taskRepository, times(1)).deleteById(1L);
    }
}