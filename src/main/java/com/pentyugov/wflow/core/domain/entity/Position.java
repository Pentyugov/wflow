package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Position")
@Table(name = "WORKFLOW_POSITION")
@Where(clause = "DELETE_DATE is null")
public class Position extends BaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

}
