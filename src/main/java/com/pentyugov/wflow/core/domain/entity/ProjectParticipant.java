package com.pentyugov.wflow.core.domain.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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

}
