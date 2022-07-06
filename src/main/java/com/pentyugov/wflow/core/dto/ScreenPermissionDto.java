package com.pentyugov.wflow.core.dto;

import com.pentyugov.wflow.core.domain.entity.Role;

public class ScreenPermissionDto extends BaseDto {

    private String screen;
    private Role role;
    private Boolean create = Boolean.FALSE;
    private Boolean read = Boolean.FALSE;
    private Boolean update = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }
}
