package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "workflow$ticket")
@Table(name = "WORKFLOW_TICKET")
@Where(clause="DELETE_DATE is null")
public class Ticket extends BaseEntity {

    public static final int CREATED = 10;
    public static final int IN_PROGRESS = 20;
    public static final int ON_HOLD = 30;
    public static final int COMPLETED = 40;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "ASSIGNEE_ID")
    private User assignee;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "EXECUTION_DATE_PLAN")
    private Date executionDatePlan;

    @Column(name = "EXECUTION_DATE_FACT")
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
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
