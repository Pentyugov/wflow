package com.pentyugov.wflow.web.payload.request;

import javax.validation.constraints.NotEmpty;

public class ResetPasswordRequest {
    @NotEmpty(message = "email cannot be empty")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
