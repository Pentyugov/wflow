package com.pentyugov.wflow.web.payload.request.telbot;

import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelbotTaskSendMessageRequest {

    public static final Integer TYPE_ASSIGNED = 10;
    public static final Integer TYPE_OVERDUE = 30;

    private Integer type;
    private Long telUserId;
    private Long telChatId;
    private TelegramTaskDto task;

}
