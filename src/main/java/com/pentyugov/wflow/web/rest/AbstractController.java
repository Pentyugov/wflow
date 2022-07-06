package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class AbstractController extends ExceptionHandling {

    protected ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse();
        body.setTimeStamp(new Date());
        body.setHttpStatus(httpStatus);
        body.setHttpStatusCode(httpStatus.value());
        body.setReason(httpStatus.getReasonPhrase().toUpperCase());
        body.setMessage(message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
