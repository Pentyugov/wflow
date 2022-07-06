package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$Department")
@Table(name = "WORKFLOW_DEPARTMENT")
@Where(clause="DELETE_DATE is null")
public class Department extends BaseEntity {

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "IS_HEAD")
    private Boolean head;

    @Column(name = "LEVEL")
    private Integer level;

    @ManyToOne
    @JoinColumn(name = "PARENT_DEPARTMENT_ID")
    private Department parentDepartment;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }
}
