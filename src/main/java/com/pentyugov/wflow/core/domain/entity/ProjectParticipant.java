package com.pentyugov.wflow.core.domain.entity;


import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$projectParticipant")
@Table(name = "WORKFLOW_PROJECT_PARTICIPANT")
@Where(clause="DELETE_DATE is null")
public class ProjectParticipant extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
