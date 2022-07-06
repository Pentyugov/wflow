package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$Note")
@Table(name = "WORKFLOW_NOTE")
@Where(clause="DELETE_DATE is null")
public class Note extends BaseEntity {

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "TITLE")
    private String title;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
