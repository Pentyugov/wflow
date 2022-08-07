package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Permission;
import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.dto.PermissionDto;
import com.pentyugov.wflow.core.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    String NAME = "wflow$RoleService";

    List<Role> getAll();

    Role getById(UUID id);

    Role add(RoleDto roleDto);

    Role update(RoleDto roleDto);

    boolean delete(Role role);

    Role convert(RoleDto roleDto);

    RoleDto convert(Role role);

    PermissionDto convertPermission(Permission permission);

    Role getByName(String name);

}
