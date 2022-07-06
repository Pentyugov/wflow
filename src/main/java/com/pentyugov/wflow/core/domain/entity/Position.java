package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "workflow$Position")
@Table(name = "WORKFLOW_POSITION")
@Where(clause="DELETE_DATE is null")
public class Position extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

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
