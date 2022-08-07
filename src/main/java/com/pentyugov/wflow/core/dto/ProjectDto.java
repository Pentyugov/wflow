package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto extends BaseDto {

    private String name;
    private String code;
    private UserDto projectManager;
    private ContractorDto contractor;
    private List<UserDto> participants;
    private Integer status;
    private Date conclusionDate;
    private Date dueDate;
    private Date closingDate;

}
