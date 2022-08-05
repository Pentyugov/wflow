package com.pentyugov.wflow.web.payload.request.telbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramVerifyCodeRequest {

    private Long telUserId;
    private String code;
}
