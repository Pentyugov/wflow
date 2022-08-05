package com.pentyugov.wflow.web.payload.response.telbot;

import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TelegramGetTaskPageResponse {

    private Integer page;
    List<TelegramTaskDto> tasks;
}
