package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "workflow$Employee")
@Table(name = "WORKFLOW_EMPLOYEE")
@Where(clause="DELETE_DATE is null")
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Date dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(Integer personnelNumber) {
        this.personnelNumber = personnelNumber;
    }
}
