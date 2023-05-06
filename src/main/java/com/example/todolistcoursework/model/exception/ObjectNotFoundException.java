package com.example.todolistcoursework.model.exception;

import com.example.todolistcoursework.security.ErrorInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ObjectNotFoundException extends GeneralExceptionBase {
    private Long timestamp;
    private ErrorInfo.ErrorType errorType;

    public ObjectNotFoundException(String message) {
        super(message);
        this.timestamp = Instant.now().toEpochMilli();
        this.errorType = ErrorInfo.ErrorType.INTERNAL;
    }
}
