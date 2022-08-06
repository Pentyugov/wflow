package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Notification")
@Table(name = "WORKFLOW_NOTIFICATION")
@Where(clause = "DELETE_DATE is null")
public class Notification extends BaseEntity {

    public static final int SUCCESS = 10;
    public static final int INFO = 20;
    public static final int WARNING = 30;
    public static final int DANGER = 40;

    public static final int SYSTEM = 10;
    public static final int CALENDAR = 20;
    public static final int TODO = 30;
    public static final int WORKFLOW = 40;


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

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

}
