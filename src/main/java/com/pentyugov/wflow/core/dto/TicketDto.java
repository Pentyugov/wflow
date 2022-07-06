package com.pentyugov.wflow.core.dto;

import java.util.Date;

public class TicketDto extends BaseDto {

    private String title;

    private String description;

    private ProjectDto project;

    private UserDto creator;

    private UserDto assignee;

    private int status;

    private Date executionDatePlan;

    private Date executionDateFact;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public UserDto getAssignee() {
        return assignee;
    }

    public void setAssignee(UserDto assignee) {
        this.assignee = assignee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
