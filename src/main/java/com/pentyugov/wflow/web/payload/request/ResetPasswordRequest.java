package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotEmpty(message = "email cannot be empty")
    private String email;

}
