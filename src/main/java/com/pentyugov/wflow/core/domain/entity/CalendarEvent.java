package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "workflow$CalendarEvent")
@Table(name = "WORKFLOW_CALENDAR_EVENT")
@Where(clause = "DELETE_DATE is null")
public class CalendarEvent extends BaseEntity {

    public static final int TYPE_TASK = 10;
    public static final int TYPE_MEETING = 20;
    public static final int TYPE_CUSTOM = 30;

    public static final String COLOR_RED_PRIMARY = "#fc4b6c";
    public static final String COLOR_RED_SECONDARY = "#f9e7eb";
    public static final String COLOR_BLUE_PRIMARY = "#1e88e5";
    public static final String COLOR_BLUE_SECONDARY = "#D1E8FF";
    public static final String COLOR_YELLOW_PRIMARY = "#ffb22b";
    public static final String COLOR_YELLOW_SECONDARY = "#FDF1BA";


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TYPE")
    private int type;

    @Column(name = "TITLE", length = 50)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

    @Column(name = "COLOR_PRIMARY")
    private String colorPrimary;

    @Column(name = "COLOR_SECONDARY")
    private String colorSecondary;

    @Column(name = "ALL_DAY")
    private Boolean allDay = Boolean.FALSE;

    @Column(name = "DRAGGABLE")
    private Boolean draggable = Boolean.FALSE;

    @Column(name = "RESIZABLE_BEFORE_START")
    private Boolean resizableBeforeStart = Boolean.FALSE;

    @Column(name = "RESIZABLE_AFTER_END")
    private Boolean resizableAfterEnd = Boolean.FALSE;

    @Column(name = "ACTIONS")
    private String actions;

}
