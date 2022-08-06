package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Note")
@Table(name = "WORKFLOW_NOTE")
@Where(clause = "DELETE_DATE is null")
public class Note extends BaseEntity {

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "COLOR")
    private String color;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
