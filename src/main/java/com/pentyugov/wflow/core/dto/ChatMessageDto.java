package com.pentyugov.wflow.core.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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
