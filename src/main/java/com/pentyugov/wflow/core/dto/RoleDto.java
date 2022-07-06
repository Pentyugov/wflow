package com.pentyugov.wflow.core.dto;

import java.util.List;

public class RoleDto extends BaseDto {

    private String name;
    private String description;
    private List<PermissionDto> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
