package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.builder.TaskMapper;
import com.example.todolistcoursework.model.dto.request.CreateTaskRequest;
import com.example.todolistcoursework.model.dto.response.TaskInfo;
import com.example.todolistcoursework.model.entity.Task;
import com.example.todolistcoursework.model.entity.User;
import com.example.todolistcoursework.service.AuthService;
import com.example.todolistcoursework.service.TaskService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TaskService taskService;
    @Autowired
    AuthService authService;

//    @Test
//    @WithMockUser(value = "user")
//    void canCreateTask() throws Exception {
//        CreateTaskRequest request = new CreateTaskRequest();
//        Task task = new Task();
//        User user = new User();
//        TaskInfo response = new TaskInfo();
//        user.setId(1L);
//        user.setUsername("u");
//        user.setEmail("e");
//        user.setRoles(Set.of());
//        user.setPassword("p");
//
//        when(taskService.createTask(user.getId(), task)).thenReturn(response);
////        when(authService.getJwtAuth().getUserId()).thenReturn(user.getId());
//
//        mockMvc.perform(post("/api/task/create"))
//                .andExpect((ResultMatcher) contentType(MediaType.APPLICATION_JSON))
//                .andExpect((ResultMatcher) content().json(asJson(response)))
//                .andExpect(status().isOk());
//
//        verify(taskService).createTask(1L, task);
//    }

    @Test
    @WithMockUser(value = "user")
    void welcomeMessage() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/task/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResultGet = mockMvc.perform(requestBuilder).andReturn();
        System.out.println(mvcResultGet.getResponse().getContentAsString());
        String responesBodyGet = mvcResultGet.getResponse().getContentAsString();
    }



    private String asJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
