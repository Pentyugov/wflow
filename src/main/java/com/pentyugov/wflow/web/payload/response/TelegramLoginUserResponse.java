package com.pentyugov.wflow.web.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramLoginUserResponse {

    private String userId;
    private String hashCode;
    private String status;
}
