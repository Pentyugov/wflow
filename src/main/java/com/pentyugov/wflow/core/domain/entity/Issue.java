package com.pentyugov.wflow.core.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "workflow$Issue")
@Table(name = "WORKFLOW_ISSUE")
@Where(clause = "DELETE_DATE is null")
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

}
