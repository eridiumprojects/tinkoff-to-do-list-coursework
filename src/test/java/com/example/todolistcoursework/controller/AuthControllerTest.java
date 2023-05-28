package com.example.todolistcoursework.controller;

import com.example.todolistcoursework.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@WithUserDetails("anton")
@TestPropertySource("/application.yaml")
class AuthControllerTest {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Test
//    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/embed.sql")
//    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/destroy.sql")
    void refreshToken() {
        assertNotNull(refreshTokenRepository.findAll());
    }
}