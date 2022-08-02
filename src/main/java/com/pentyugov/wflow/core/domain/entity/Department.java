package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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

}
