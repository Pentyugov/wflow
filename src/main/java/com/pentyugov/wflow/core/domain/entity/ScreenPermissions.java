package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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

}
