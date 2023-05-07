package com.example.todolistcoursework.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorInfo {

    private Long timestamp;
    private String message;
    private ErrorType type;

    public ErrorInfo(Long timestamp, ErrorType errorType, String message) {
        this.timestamp = timestamp;
        this.type = errorType;
        this.message = message;
    }

    public ErrorInfo(Long timestamp, ErrorType errorType) {
        this.timestamp = timestamp;
        this.type = errorType;
    }


    public enum ErrorType {
        CLIENT("client"),

        VALIDATION("validation"),
        AUTH("auth"),

        SERVER("server");
        private final String value;

        ErrorType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ErrorType fromValue(String value) {
            for (ErrorType b : ErrorType.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

}
