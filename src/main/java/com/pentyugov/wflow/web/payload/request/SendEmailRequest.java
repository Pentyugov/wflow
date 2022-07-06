package com.pentyugov.wflow.web.payload.request;

import com.pentyugov.wflow.core.annotation.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SendEmailRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String receiver;

    private String subject;

    private String body;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
