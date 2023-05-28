//package com.example.todolistcoursework.controller;
//
//import com.example.todolistcoursework.builder.TaskMapper;
//import com.example.todolistcoursework.model.dto.request.CreateTaskRequest;
//import com.example.todolistcoursework.model.dto.response.TaskInfo;
//import com.example.todolistcoursework.model.entity.Task;
//import com.example.todolistcoursework.model.entity.User;
//import com.example.todolistcoursework.model.enums.TaskStatus;
//import com.example.todolistcoursework.repository.TaskRepository;
//import com.example.todolistcoursework.service.AuthService;
//import com.example.todolistcoursework.service.TaskService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.checkerframework.checker.units.qual.C;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc(printOnlyOnFailure = false)
//@WithMockUser
//class TaskControllerTest {
//    @Autowired
//    ObjectMapper mapper;
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    TaskService taskService;
//    @Autowired
//    AuthService authService;
//    @Autowired
//    TaskRepository taskRepository;
//
//    public CreateTaskRequest createTaskRequestByDefault() {
//        CreateTaskRequest request = new CreateTaskRequest();
//        request.setData("data");
//        request.setDeadline(LocalDateTime.MAX);
//        request.setDescription("desc");
//        return request;
//    }
//
//    public User createUserByDefault() {
//        return User.builder()
//                .id(1L)
//                .firstName("Anton")
//                .lastName("Pestrikov")
//                .username("eridium")
//                .email("pestrikov@mail.ru")
//                .password("pestrikov123")
//                .devices(List.of())
//                .build();
//    }
//
//    public Task createTaskByDefault() {
//        return Task.builder()
//                .id(1L)
//                .data("new")
//                .description("desc")
//                .created(LocalDateTime.now())
//                .deadline(LocalDateTime.MAX)
//                .status(TaskStatus.BACKLOG)
//                .user(createUserByDefault())
//                .build();
//    }
//
//    public TaskInfo createTaskResponseByDefault() {
//        return TaskMapper.toApi(createTaskByDefault());
//    }
//
//    @Test
//    void createTask() throws Exception {
//        Task task = createTaskByDefault();
//        //given
//        var requestBuilder = post("/api/task/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                            {
//                            "data": "new",
//                            "description": "new"
//                            }
//                        """);
//        //when
//        this.mockMvc.perform(requestBuilder)
//                .andExpectAll(
//                        status().isOk(),
//                        header().exists(HttpHeaders.LOCATION),
//                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
//                        MockMvcResultMatchers.content().json("""
//                                {
//                                "id": 1,
//                                "data": "new",
//                                "description": "new",
//                                "status": "BACKLOG"
//                                }
//                                    """));
//
//        when(taskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(task));
//        assertEquals(taskRepository.findById(1L), Optional.ofNullable(task));
//
//        //then
//    }
//
//
////    @Test
////    void createTask() throws Exception {
////        //given
////        var requestBuilder = post("/api/task/create")
////                .contentType(MediaType.APPLICATION_JSON)
////                .accept(MediaType.APPLICATION_JSON)
////                .content(mapper.writeValueAsString(createTaskRequestByDefault()));
////
////        //when
////        this.mockMvc.perform(requestBuilder)
////                .andExpectAll(
////                        status().isOk(),
////                        (ResultMatcher) content().contentType(MediaType.APPLICATION_JSON),
////                        content().json(create)
////                );
////        //then
////    }
//
//    @Test
//    void getTask() {
//    }
//
//    @Test
//    void updateTask() {
//    }
//
//    @Test
//    void deleteTask() {
//    }
//
//    @Test
//    void getTasks() {
//    }
//
//    @Test
//    void searchTasks() {
//    }
//}