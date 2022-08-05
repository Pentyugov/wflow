package com.pentyugov.wflow.web.payload.response.telbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class TelegramVerifyCodeResponse {
    private boolean verified = false;
    private HttpStatus httpStatus;
}
