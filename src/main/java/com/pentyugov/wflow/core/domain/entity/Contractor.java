package com.pentyugov.wflow.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getNonResident() {
        return nonResident;
    }

    public void setNonResident(Boolean nonResident) {
        this.nonResident = nonResident;
    }

    public Boolean getSupplier() {
        return supplier;
    }

    public void setSupplier(Boolean supplier) {
        this.supplier = supplier;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public Boolean getOrganization() {
        return isOrganization;
    }

    public void setOrganization(Boolean organization) {
        isOrganization = organization;
    }
}
