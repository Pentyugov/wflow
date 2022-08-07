package com.pentyugov.wflow.web.exception;

public class EmailIsEmptyException  extends RuntimeException {
    public EmailIsEmptyException(String message) {
        super(message);
    }
}
