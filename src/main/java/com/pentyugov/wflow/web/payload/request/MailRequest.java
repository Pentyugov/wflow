package com.pentyugov.wflow.web.payload.request;

import com.pentyugov.wflow.core.annotation.ValidEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MailRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String to;
    private String recipientName;
    private String subject;
    private String text;
    private String senderName;

}
