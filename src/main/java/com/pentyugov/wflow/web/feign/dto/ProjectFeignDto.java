package com.pentyugov.wflow.web.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFeignDto extends CardFeignDto {

    private String name;
    private String code;
    private Integer status;
    private Date conclusionDate;
    private Date dueDate;
    private Date closingDate;
    private String projectManagerId;
    private List<UUID> projectParticipantsIds;
    private UUID contractorId;

}
