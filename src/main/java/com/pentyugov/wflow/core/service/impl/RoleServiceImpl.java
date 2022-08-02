package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Permission;
import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.PermissionDto;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.repository.PermissionRepository;
import com.pentyugov.wflow.core.repository.RoleRepository;
import com.pentyugov.wflow.core.repository.UserRepository;
import com.pentyugov.wflow.core.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service(RoleService.NAME)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createNewRole(RoleDto roleDto) {
        Role role = createRoleFromProxy(roleDto);
        return roleRepository.save(role);
    }

    public Role updateRole(RoleDto roleDto) {
        Role role = createRoleFromProxy(roleDto);
        return roleRepository.save(role);
    }

    public Role createRoleFromProxy(RoleDto roleDto) {
        Role role = null;
        if (!ObjectUtils.isEmpty(roleDto.getId())) {
            role = roleRepository.getById(roleDto.getId());
        }

        if (ObjectUtils.isEmpty(role)) {
            role = new Role();
            role.setId(roleDto.getId());
        }

        roleDto.setName(roleDto.getName().toUpperCase());
        if (!roleDto.getName().startsWith(Role.PREFIX)) {
            roleDto.setName(Role.PREFIX + roleDto.getName());
        }
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        List<Permission> permissions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleDto.getPermissions())) {
            roleDto.getPermissions().forEach(permissionDto ->
                    permissions.add(permissionRepository.getById(permissionDto.getId())));
        }
        role.setPermissions(permissions);

        return role;
    }

    public RoleDto createProxyFromRole(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName().substring(5));
        roleDto.setDescription(role.getDescription());
        List<PermissionDto> permissions = new ArrayList<>();
        role.getPermissions().forEach(permission -> permissions.add(createProxyFromPermission(permission)));
        roleDto.setPermissions(permissions);
        return roleDto;
    }

    public PermissionDto createProxyFromPermission(Permission permission) {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(permission.getId());
        permissionDto.setName(permission.getName());
        return permissionDto;
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public Role getRoleById(UUID id) {
        return roleRepository.findById(id).orElse(null);
    }

    public boolean deleteRole(Role role) {
        for (User user : userRepository.findAll()) {
            for (Role r : user.getRoles()) {
                if (r.getId().equals(role.getId())) {
                    return false;
                }
            }
        }
        roleRepository.delete(role.getId());
        return true;
    }
}
