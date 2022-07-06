package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$Card")
@Table(name = "WORKFLOW_CARD")
@Where(clause="DELETE_DATE is null")
@Inheritance(strategy = InheritanceType.JOINED)
public class Card extends BaseEntity {

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "ISSUE_ID")
    private Issue issue;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
