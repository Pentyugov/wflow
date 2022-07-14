package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "workflow$project")
@Table(name = "WORKFLOW_PROJECT")
@Where(clause="DELETE_DATE is null")
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

    public List<ProjectParticipant> getProjectParticipants() {
        return projectParticipants;
    }

    public void setProjectParticipants(List<ProjectParticipant> projectParticipants) {
        this.projectParticipants = projectParticipants;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Date getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(Date conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
