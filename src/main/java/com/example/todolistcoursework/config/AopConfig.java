package com.example.todolistcoursework.config;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class AopConfig {
    @Before("execution(* com.example.todolistcoursework.repository.TaskRepository.save(..))")
    public void beforeCreatedTaskLog() {
        log.info("The user is trying to create a task.");
    }
    @After("execution(* com.example.todolistcoursework.repository.TaskRepository.save(..))")
    public void createdTaskLog() {
        log.info("The user has been created a task.");
    }
    @Before("execution(* com.example.todolistcoursework.repository.UserRepository.save(..))")
    public void beforeRegisterUserLog() {
        log.info("The user is trying to register.");
    }
    @After("execution(* com.example.todolistcoursework.repository.UserRepository.save(..))")
    public void registerUserLog() {
        log.info("The user has been registered.");
    }
    @Before("execution(* com.example.todolistcoursework.service.UserService.loginUser(..))")
    public void beforeLoginUserLog() {
        log.info("The user is trying to login into account.");
    }
    @After("execution(* com.example.todolistcoursework.service.UserService.loginUser(..))")
    public void loginUserLog() {
        log.info("The user has been logged into account.");
    }
    @After("execution(* com.example.todolistcoursework.repository.RefreshTokenRepository.save(..))")
    public void refreshTokenLog() {
        log.info("The refresh token has been generated.");
    }
    @After("execution(* com.example.todolistcoursework.repository.DeviceRepository.save(..))")
    public void deviceLog() {
        log.info("The device has been saved.");
    }
    @Before("execution(* com.example.todolistcoursework.service.UserService.getUserInfo(..))")
    public void beforeUserInfoLog() {
        log.info("The user is trying to receive his data.");
    }
    @After("execution(* com.example.todolistcoursework.service.UserService.getUserInfo(..))")
    public void userInfoLog() {
        log.info("The user has been received his data.");
    }
    @Before("execution(* com.example.todolistcoursework.service.UserService.deleteUser(..))")
    public void beforeUserDeleteLog() {
        log.info("The user is trying to delete his account.");
    }
    @After("execution(* com.example.todolistcoursework.service.UserService.getUserInfo(..))")
    public void userDeleteLog() {
        log.info("The user has been deleted his account.");
    }

}
