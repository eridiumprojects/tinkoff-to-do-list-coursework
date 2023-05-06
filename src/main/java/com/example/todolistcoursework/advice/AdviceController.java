package com.example.todolistcoursework.advice;

import com.example.todolistcoursework.model.exception.AuthException;
import com.example.todolistcoursework.model.exception.GeneralExceptionBase;
import com.example.todolistcoursework.model.exception.ObjectAlreadyExists;
import com.example.todolistcoursework.model.exception.ObjectNotFoundException;
import com.example.todolistcoursework.security.ErrorInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    @Nullable
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        logger.warn(ex.getMessage(), ex);

        var errorInfo = new ErrorInfo(
                Instant.now().toEpochMilli(),
                ErrorInfo.ErrorType.CLIENT,
                ex.getMessage(),
                ex.getCause().toString());

        return super.handleExceptionInternal(ex, errorInfo, headers, statusCode, request);
    }


    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ObjectNotFoundException ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getErrorType(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ObjectAlreadyExists.class)
    public ResponseEntity<Object> handleAlreadyExists(ObjectAlreadyExists ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getErrorType(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AuthException.class})
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getErrorType(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAll(RuntimeException ex) {
        var errorInfo = new ErrorInfo(
                Instant.now().toEpochMilli(),
                ErrorInfo.ErrorType.INTERNAL,
                ex.getMessage(),
                ex.getCause().toString());
        return new ResponseEntity<>(
                errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}