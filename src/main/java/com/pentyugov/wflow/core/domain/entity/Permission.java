package com.pentyugov.wflow.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "sec$Permission")
@Table(name = "SECURITY_PERMISSION")
public class Permission extends BaseEntity {

    public static final String READ = "READ";
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String SEND_SYS_MAIL = "SEND_SYS_MAIL";

    @Column(name = "NAME")
    private String name;

    public Permission() {

    }

    public Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
