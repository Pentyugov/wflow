package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto extends BaseDto {

    private String firstName;
    private String lastName;
    private String middleName;
    private BigDecimal salary;
    private String phoneNumber;
    private String email;
    private Date hireDate;
    private Date dismissalDate;
    private Boolean head;
    private UserDto user;
    private PositionDto position;
    private DepartmentDto department;
    private Integer personnelNumber;

}
