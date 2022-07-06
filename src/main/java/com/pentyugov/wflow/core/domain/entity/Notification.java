package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$Notification")
@Table(name = "WORKFLOW_NOTIFICATION")
@Where(clause="DELETE_DATE is null")
public class Notification extends BaseEntity {

    public static int SUCCESS = 10;
    public static int INFO = 20;
    public static int WARNING = 30;
    public static int DANGER = 40;

    public static int SYSTEM = 10;
    public static int CALENDAR = 20;
    public static int TODO = 30;
    public static int WORKFLOW = 40;


    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "TYPE", nullable = false)
    private int type;

    @Column(name = "ACCESSORY_TYPE")
    private int accessoryType;

    @Column(name = "READ")
    private Boolean read;

    @ManyToOne
    @JoinColumn(name = "RECEIVER_ID")
    private User receiver;

    public int getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(int accessoryType) {
        this.accessoryType = accessoryType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
