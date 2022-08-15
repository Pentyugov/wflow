package com.pentyugov.wflow.web.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFeignDto extends FeignDto {

    private ProjectFeignDto project;
    private String priority;
    private Boolean started;
    private Boolean overdue = Boolean.FALSE;
    private Date executionDatePlan;
    private Date executionDateFact;
    private String kanbanState;
    private Integer kanbanOrder;
    private UUID executorId;
    private UUID initiatorId;

}
