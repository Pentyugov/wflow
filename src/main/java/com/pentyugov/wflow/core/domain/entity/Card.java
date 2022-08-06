package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Card")
@Table(name = "WORKFLOW_CARD")
@Where(clause = "DELETE_DATE is null")
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
    @JoinColumn(name = "PARENT_CARD_ID")
    private Card parentCard;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "ISSUE_ID")
    private Issue issue;

}
