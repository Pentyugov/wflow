package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto extends BaseDto  {
    private String content;

    private int status;
    private String senderId;
    private String recipientId;
    private UserDto sender;
    private UserDto recipient;
    private String chatId;
    private Date createDate;

}
