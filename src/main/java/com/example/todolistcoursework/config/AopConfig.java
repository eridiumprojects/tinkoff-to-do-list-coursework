package com.example.todolistcoursework.config;

import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.model.exception.ObjectAlreadyExists;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class AopConfig {
    @Before("execution(* com.example.todolistcoursework.service.TaskService.createTask(..))")
    public void beforeCreatedTaskLog() {
        log.info("The user is trying to create a task.");
    }

    @AfterReturning("execution(* com.example.todolistcoursework.service.TaskService.createTask(..))")
    public void createdTaskLog() {
        log.info("The user has been created a task.");
    }

    @Before("execution(* com.example.todolistcoursework.service.UserService.registerUser(..))")
    public void beforeRegisterUserLog() {
        log.info("The user is trying to register.");
    }

    @Around("execution(* com.example.todolistcoursework.service.UserService.registerUser(..))")
    public void registerUserLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object user = null;
        try {
            user = proceedingJoinPoint.proceed();
            log.info("The user has been registered.");
        } catch (ObjectAlreadyExists e) {
            log.error("The user with these details is already exists.");
            throw e;
        }
    }

    @Before("execution(* com.example.todolistcoursework.service.UserService.loginUser(..))")
    public void beforeLoginUserLog() {
        log.info("The user is trying to login into account.");
    }

    @Around("execution(* com.example.todolistcoursework.service.UserService.loginUser(..))")
    public void loginUserLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object user = null;
        try {
            user = proceedingJoinPoint.proceed();
            log.info("The user has been logged into account.");
        } catch (AuthException e) {
            log.error("The user inputs incorrect password. Access to the account is forbidden.");
            throw e;
        }
    }

    @After("execution(* com.example.todolistcoursework.service.JwtService.generateAccessToken(..))")
    public void accessTokenLog() {
        log.info("The access token has been generated.");
    }

    @After("execution(* com.example.todolistcoursework.service.JwtService.generateRefreshToken(..))")
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

    @AfterReturning("execution(* com.example.todolistcoursework.service.UserService.getUserInfo(..))")
    public void userInfoLog() {
        log.info("The user has been received his data.");
    }

    @Before("execution(* com.example.todolistcoursework.service.UserService.deleteUser(..))")
    public void beforeUserDeleteLog() {
        log.info("The user is trying to delete his account.");
    }

    @AfterReturning("execution(* com.example.todolistcoursework.service.UserService.getUserInfo(..))")
    public void userDeleteLog() {
        log.info("The user has been deleted his account.");
    }

    @Around("execution(* com.example.todolistcoursework.service.TaskService.updateTask(..))")
    public void taskUpdateLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object task = null;
        try {
            task = proceedingJoinPoint.proceed();
            log.info("The user has been updated his task.");

        } catch (ObjectNotFoundException e) {
            log.error("The user does not have such task");
            throw e;
        }
    }

    @Around("execution(* com.example.todolistcoursework.service.TaskService.deleteTask(..))")
    public void taskDeleteLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object task = null;
        try {
            task = proceedingJoinPoint.proceed();
            log.info("The user has been deleted his task.");
        } catch (ObjectNotFoundException e) {
            log.error("The user does not have such task");
            throw e;
        }
    }

    @Around("execution(* com.example.todolistcoursework.service.TaskService.tickTask(..))")
    public void taskTickLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object task = null;
        try {
            task = proceedingJoinPoint.proceed();
            log.info("The user has been ticked his task.");
        } catch (ObjectNotFoundException e) {
            log.error("The user does not have such task");
            throw e;
        }
    }

    @After("execution(* com.example.todolistcoursework.service.TaskService.getTasks(..))")
    public void allTasksLog() {
        log.info("The user has been received the list of his all tasks.");
    }

    @AfterReturning("execution(* com.example.todolistcoursework.service.TaskService.getActualTasks(..))")
    public void actualTasksLog() {
        log.info("The user has been received the list of his actual tasks.");
    }

    @AfterReturning("execution(* com.example.todolistcoursework.service.TaskService.getCompletedTasks(..))")
    public void completedTasksLog() {
        log.info("The user has been received the list of his completed tasks.");
    }


}
