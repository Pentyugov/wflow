package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Permission;
import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.dto.PermissionDto;
import com.pentyugov.wflow.core.dto.RoleDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    String NAME = "wflow$RoleService";

    List<Role> getAllRoles();

    Role createNewRole(RoleDto roleDto);

    Role updateRole(RoleDto roleDto);

    Role createRoleFromProxy(RoleDto roleDto);

    RoleDto createProxyFromRole(Role role);

    PermissionDto createProxyFromPermission(Permission permission);

    Role getRoleByName(String name);

    Role getRoleById(UUID id);

    boolean deleteRole(Role role);
}
