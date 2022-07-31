package com.pentyugov.wflow.web.payload.request;

import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramOverdueTasksRequest {

    private Long telUserId;
    private Long telChatId;
    private TelegramTaskDto task;

}
