package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TicketDto extends BaseDto {

    private String title;
    private String description;
    private ProjectDto project;
    private UserDto creator;
    private UserDto assignee;
    private int status;
    private Date executionDatePlan;
    private Date executionDateFact;

}
