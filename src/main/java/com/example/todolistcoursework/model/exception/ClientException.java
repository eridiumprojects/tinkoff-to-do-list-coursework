package com.example.todolistcoursework.model.exception;

import com.example.todolistcoursework.security.ErrorInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ClientException extends RuntimeException {
    private Long timestamp;
    private ErrorInfo.ErrorType errorType;
    private String errorMessage;

    public ClientException(String message) {
        super(message);
        this.timestamp = Instant.now().toEpochMilli();
        this.errorType = ErrorInfo.ErrorType.CLIENT;
    }
}
