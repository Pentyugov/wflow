package com.pentyugov.wflow.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "sec$Role")
@Table(name = "SECURITY_ROLE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String SECRETARY = "ROLE_SECRETARY";
    public static final String PROJECT_MANAGER = "ROLE_PROJECT_MANAGER";
    public static final String PROJECT_PARTICIPANT = "ROLE_PROJECT_PARTICIPANT";
    public static final String TASK_INITIATOR = "ROLE_TASK_INITIATOR";
    public static final String TASK_EXECUTOR = "ROLE_TASK_EXECUTOR";
    public static final String PREFIX = "ROLE_";

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "security_role_permission",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Collection<Permission> permissions;

    public Role(String name) {
        this.name = name;
    }

}

