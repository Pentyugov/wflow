package com.pentyugov.wflow.core.domain.entity;


import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$Issue")
@Table(name = "WORKFLOW_ISSUE")
@Where(clause="DELETE_DATE is null")
public class Issue extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "EXECUTOR_ID")
    private User executor;

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
