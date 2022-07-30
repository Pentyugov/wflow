package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentDto extends BaseDto {

    private String name;
    private String code;
    private Boolean head;
    private Integer level;
    private DepartmentDto parentDepartment;

}
