package com.pentyugov.wflow.web.payload.request;

import com.pentyugov.wflow.core.annotation.PasswordMatchers;
import com.pentyugov.wflow.core.annotation.ValidEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatchers
public class SignUpRequest {

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8)
    private String password;
    private String confirmPassword;

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    private String firstname;
    private String lastname;

}
