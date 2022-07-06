package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "security$ScreenPermissions")
@Table(name = "SECURITY_SCREEN_PERMISSIONS")
@Where(clause="DELETE_DATE is null")
public class ScreenPermissions extends BaseEntity {

    @Column(name = "SCREEN")
    private String screen;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Column(name = "CAN_CREATE")
    private Boolean create = Boolean.FALSE;

    @Column(name = "CAN_READ")
    private Boolean read = Boolean.FALSE;

    @Column(name = "CAN_UPDATE")
    private Boolean update = Boolean.FALSE;

    @Column(name = "CAN_DELETE")
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
