package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TelegramTaskDto {

    private String number;
    private String description;
    private String priority;
    private String project;
    private Date dueDate;
}
