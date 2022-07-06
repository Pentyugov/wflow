package com.pentyugov.wflow.core.dto;

import java.util.Date;
import java.util.List;

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

    public Date getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(Date conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public ContractorDto getContractor() {
        return contractor;
    }

    public void setContractor(ContractorDto contractor) {
        this.contractor = contractor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserDto getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(UserDto projectManager) {
        this.projectManager = projectManager;
    }

    public List<UserDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDto> participants) {
        this.participants = participants;
    }
}
