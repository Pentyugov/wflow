package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto extends BaseDto {

    private ProjectDto project;
    private String priority;
    private long daysUntilDueDate;
    private String number;
    private String description;
    private String comment;
    private String state;
    private String kanbanState;
    private Integer kanbanOrder;
    private Date executionDatePlan;
    private Date executionDateFact;
    private Boolean started;
    private Boolean overdue;
    private UserDto creator;
    private UserDto executor;
    private UserDto initiator;

}
