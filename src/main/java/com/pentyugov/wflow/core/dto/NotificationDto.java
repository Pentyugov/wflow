package com.pentyugov.wflow.core.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class NotificationDto extends BaseDto {

    private Date createDate;
    private String title;
    private String message;
    private int type;
    private int accessoryType;
    private Boolean read;
    private UUID receiverId;
    private UUID cardId;

}
