package com.pentyugov.wflow.core.dto;

public class DepartmentDto extends BaseDto {

    private String name;

    private String code;

    private Boolean head;

    private Integer level;

    private DepartmentDto parentDepartment;

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

    public DepartmentDto getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(DepartmentDto parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Boolean getHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
