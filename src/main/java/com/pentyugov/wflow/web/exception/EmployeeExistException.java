package com.pentyugov.wflow.web.exception;

public class EmployeeExistException extends RuntimeException {
    public EmployeeExistException(String message) {
        super(message);
    }
}
