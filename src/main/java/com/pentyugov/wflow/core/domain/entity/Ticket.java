package com.pentyugov.wflow.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workflow$ticket")
@Table(name = "WORKFLOW_TICKET")
@Where(clause = "DELETE_DATE is null")
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

}
