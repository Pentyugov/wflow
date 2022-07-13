package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "workflow$Task")
@Table(name = "WORKFLOW_TASK")
@Where(clause="DELETE_DATE is null")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "ID")
public class Task extends Card {

    @Transient
    public static final String PRIORITY_LOW = "PRIORITY$LOW";
    @Transient
    public static final String PRIORITY_MEDIUM = "PRIORITY$MEDIUM";
    @Transient
    public static final String PRIORITY_HIGH = "PRIORITY$HIGH";

    @Transient
    public static final String STATE_CREATED = "TS$CREATED";
    @Transient
    public static final String STATE_ASSIGNED = "TS$ASSIGNED";
    @Transient
    public static final String STATE_FINISHED = "TS$FINISHED";
    @Transient
    public static final String STATE_CLOSED = "TS$CLOSED";
    @Transient
    public static final String STATE_CANCELED = "TS$CANCELED";
    @Transient
    public static final String STATE_EXECUTED = "TS$EXECUTED";
    @Transient
    public static final String STATE_REWORK = "TS$REWORK";

    @Transient
    public static final String ACTION_START = "START";
    @Transient
    public static final String ACTION_FINISH = "FINISH";
    @Transient
    public static final String ACTION_EXECUTE = "EXECUTE";
    @Transient
    public static final String ACTION_CANCEL = "CANCEL";
    @Transient
    public static final String ACTION_REWORK = "REWORK";

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "STARTED")
    private Boolean started;

    @Column(name = "OVERDUE")
    private Boolean overdue = Boolean.FALSE;

    @Column(name = "EXECUTION_DATE_PLAN")
    private Date executionDatePlan;

    @Column(name = "EXECUTION_DATE_FACT")
    private Date executionDateFact;

    @ManyToOne
    @JoinColumn(name = "EXECUTOR_ID")
    private User executor;

    @ManyToOne
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
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

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }
}
