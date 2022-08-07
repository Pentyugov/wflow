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
public class DepartmentDto extends BaseDto {

    private String name;
    private String code;
    private Boolean head;
    private Integer level;
    private DepartmentDto parentDepartment;

}
