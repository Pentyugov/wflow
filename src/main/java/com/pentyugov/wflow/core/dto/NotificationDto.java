package com.pentyugov.wflow.core.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
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
