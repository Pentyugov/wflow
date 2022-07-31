package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramLoginUserRequest {

    private String username;
    private Long telUserId;
    private Long telChatId;
}
