package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TelegramTaskDto {

    private UUID id;
    private String number;
    private String description;
    private String priority;
    private String project;
    private String comment;
    private Date dueDate;
}
