package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "workflow$Task")
@Table(name = "WORKFLOW_TASK")
@Where(clause="DELETE_DATE is null")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "ID")
@Getter
@Setter
@NoArgsConstructor
public class Task extends Card {

    @Transient
    public static final String PRIORITY_LOW = "PRIORITY$LOW";
    @Transient
    public static final String PRIORITY_MEDIUM = "PRIORITY$MEDIUM";
    @Transient
    public static final String PRIORITY_HIGH = "PRIORITY$HIGH";

    @Transient
    public static final String STATE_ACTIVE = "TS$ACTIVE";
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
    public static final String KANBAN_STATE_NEW = "KB$NEW";
    @Transient
    public static final String KANBAN_STATE_IN_PROGRESS = "KB$IN_PROGRESS";
    @Transient
    public static final String KANBAN_STATE_ON_HOLD = "KB$ON_HOLD";
    @Transient
    public static final String KANBAN_STATE_COMPLETED = "KB$COMPLETED";

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

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

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

    @Column(name = "KANBAN_STATE")
    private String kanbanState;

    @Column(name = "KANBAN_ORDER")
    private Integer kanbanOrder;

    @ManyToOne
    @JoinColumn(name = "EXECUTOR_ID")
    private User executor;

    @ManyToOne
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;

}
