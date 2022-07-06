package com.pentyugov.wflow.core.dto;

import java.util.Date;

public class TaskDto extends BaseDto {

    private String priority;

    private long daysUntilDueDate;

    private String number;

    private String description;

    private String comment;

    private String state;

    private Date executionDatePlan;

    private Date executionDateFact;

    private Boolean started;

    private Boolean overdue;

    private UserDto creator;

    private UserDto executor;

    private UserDto initiator;

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getExecutionDatePlan() {
        return executionDatePlan;
    }

    public void setExecutionDatePlan(Date executionDatePlan) {
        this.executionDatePlan = executionDatePlan;
    }

    public Date getExecutionDateFact() {
        return executionDateFact;
    }

    public void setExecutionDateFact(Date executionDateFact) {
        this.executionDateFact = executionDateFact;
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public UserDto getExecutor() {
        return executor;
    }

    public void setExecutor(UserDto executor) {
        this.executor = executor;
    }

    public UserDto getInitiator() {
        return initiator;
    }

    public void setInitiator(UserDto initiator) {
        this.initiator = initiator;
    }

    public long getDaysUntilDueDate() {
        return daysUntilDueDate;
    }

    public void setDaysUntilDueDate(long daysUntilDueDate) {
        this.daysUntilDueDate = daysUntilDueDate;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }
}
