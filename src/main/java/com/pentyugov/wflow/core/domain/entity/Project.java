package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$project")
@Table(name = "WORKFLOW_PROJECT")
@Where(clause = "DELETE_DATE is null")
@PrimaryKeyJoinColumn(name = "CARD_ID", referencedColumnName = "ID")
public class Project extends Card {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "CONCLUSION_DATE")
    private Date conclusionDate;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "CLOSING_DATE")
    private Date closingDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PROJECT_MANAGER_ID")
    private User projectManager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    private List<ProjectParticipant> projectParticipants;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

}
