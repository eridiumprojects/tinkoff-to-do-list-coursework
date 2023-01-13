package com.example.todolistcoursework.exceptions;

public class GeneralExceptionBase extends RuntimeException{
    public GeneralExceptionBase() {
        super();
    }

    public GeneralExceptionBase(String message) {
        super(message);
    }
}
