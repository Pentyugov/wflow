package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Employee")
@Table(name = "WORKFLOW_EMPLOYEE")
@Where(clause = "DELETE_DATE is null")
public class Employee extends BaseEntity {

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "SALARY")
    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "POSITION_ID")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "HIRE_DATE")
    private Date hireDate;

    @Column(name = "DISMISSAL_DATE")
    private Date dismissalDate;

    @Column(name = "HEAD")
    private Boolean head;

    @Column(name = "PERSONNEL_NUMBER")
    private Integer personnelNumber;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
