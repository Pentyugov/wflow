package com.pentyugov.wflow.web.payload.request;

import com.pentyugov.wflow.core.annotation.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MailRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String to;
    private String recipientName;
    private String subject;
    private String text;
    private String senderName;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
