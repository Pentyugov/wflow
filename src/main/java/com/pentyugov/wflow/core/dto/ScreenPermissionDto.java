package com.pentyugov.wflow.core.dto;

import com.pentyugov.wflow.core.domain.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ScreenPermissionDto extends BaseDto {

    private String screen;
    private Role role;
    private Boolean create = Boolean.FALSE;
    private Boolean read = Boolean.FALSE;
    private Boolean update = Boolean.FALSE;
    private Boolean delete = Boolean.FALSE;

}
