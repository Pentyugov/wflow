package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
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
