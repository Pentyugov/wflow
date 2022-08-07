package com.pentyugov.wflow.web.exception;

public class UsernameIsEmptyException extends RuntimeException {
    public UsernameIsEmptyException(String message) {
        super(message);
    }
}
