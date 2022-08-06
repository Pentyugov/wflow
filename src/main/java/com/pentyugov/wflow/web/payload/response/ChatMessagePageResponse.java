package com.pentyugov.wflow.web.payload.response;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessagePageResponse {

    private int totalPages;
    private List<ChatMessageDto> messages;

}
