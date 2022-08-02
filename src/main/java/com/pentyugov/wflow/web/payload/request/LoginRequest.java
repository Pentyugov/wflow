package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "username cannot be empty")
    private String username;
    @NotEmpty(message = "password cannot be empty")
    private String password;

}
