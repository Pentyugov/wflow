package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "workflow$CalendarEvent")
@Table(name = "WORKFLOW_CALENDAR_EVENT")
@Where(clause="DELETE_DATE is null")
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public Boolean getResizableBeforeStart() {
        return resizableBeforeStart;
    }

    public void setResizableBeforeStart(Boolean resizableBeforeStart) {
        this.resizableBeforeStart = resizableBeforeStart;
    }

    public Boolean getResizableAfterEnd() {
        return resizableAfterEnd;
    }

    public void setResizableAfterEnd(Boolean resizableAfterEnd) {
        this.resizableAfterEnd = resizableAfterEnd;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public String getColorSecondary() {
        return colorSecondary;
    }

    public void setColorSecondary(String colorSecondary) {
        this.colorSecondary = colorSecondary;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }
}
