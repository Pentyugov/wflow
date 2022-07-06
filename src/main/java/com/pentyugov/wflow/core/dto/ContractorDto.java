package com.pentyugov.wflow.core.dto;

public class ContractorDto extends BaseDto {

    protected String name;

    protected String fullName;

    protected String okpo;

    protected String kpp;

    protected String inn;

    protected String postalAddress;

    protected String legalAddress;

    protected String phone;

    protected String fax;

    protected String email;

    protected String comment;

    protected String website;

    protected Boolean nonResident = Boolean.FALSE;

    protected Boolean supplier = Boolean.FALSE;

    protected Boolean customer = Boolean.FALSE;

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

    public Boolean getIsOrganization() {
        return isOrganization;
    }

    public void setIsOrganization(Boolean organization) {
        isOrganization = organization;
    }
}
