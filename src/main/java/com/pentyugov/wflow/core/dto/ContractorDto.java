package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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

}
