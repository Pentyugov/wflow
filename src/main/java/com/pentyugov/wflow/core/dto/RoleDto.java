package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseDto {

    private String name;
    private String description;
    private List<PermissionDto> permissions;

}
