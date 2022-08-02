package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$Contractor")
@Table(name = "WORKFLOW_CONTRACTOR")
public class Contractor extends BaseEntity {

    @Column(name = "NAME")
    protected String name;

    @Column(name = "FULL_NAME")
    protected String fullName;

    @Column(name = "OKPO", length = 10)
    protected String okpo;

    @Column(name = "KPP", length = 9)
    protected String kpp;

    @Column(name = "INN", length = 12)
    protected String inn;

    @Column(name = "POSTAL_ADDRESS", length = 300)
    protected String postalAddress;

    @Column(name = "LEGAL_ADDRESS", length = 300)
    protected String legalAddress;

    @Column(name = "PHONE")
    protected String phone;

    @Column(name = "FAX")
    protected String fax;

    @Column(name = "EMAIL")
    protected String email;

    @Column(name = "COMMENT_", length = 1000)
    protected String comment;

    @Column(name = "WEBSITE")
    protected String website;

    @Column(name = "NON_RESIDENT")
    protected Boolean nonResident = Boolean.FALSE;

    @Column(name = "SUPPLIER")
    protected Boolean supplier = Boolean.FALSE;

    @Column(name = "CUSTOMER")
    protected Boolean customer = Boolean.FALSE;

    @Column(name = "ORGANIZATION")
    protected Boolean isOrganization = Boolean.FALSE;

}
