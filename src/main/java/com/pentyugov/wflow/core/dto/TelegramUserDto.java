package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TelegramUserDto {

    private UUID userId;
    private Long telUserId;
    private Long telChatId;
    private Boolean telLogged = Boolean.FALSE;

}
