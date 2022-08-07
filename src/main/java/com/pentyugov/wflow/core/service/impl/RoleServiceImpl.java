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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(RoleService.NAME)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getById(UUID id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role add(RoleDto roleDto) {
        Role role = convert(roleDto);
        return roleRepository.save(role);
    }

    @Override
    public Role update(RoleDto roleDto) {
        Role role = convert(roleDto);
        return roleRepository.save(role);
    }

    @Override
    public boolean delete(Role role) {
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

    @Override
    public Role convert(RoleDto roleDto) {
        Role role = roleRepository.findById(roleDto.getId()).orElse(new Role());
        roleDto.setName(roleDto.getName().toUpperCase());
        if (!roleDto.getName().startsWith(Role.PREFIX)) {
            roleDto.setName(Role.PREFIX + roleDto.getName());
        }
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        role.setPermissions(
                roleDto.getPermissions().stream().map(this::findPermissionById).collect(Collectors.toList())
        );

        return role;
    }

    @Override
    public RoleDto convert(Role role) {
        return RoleDto
                .builder()
                .id(role.getId())
                .name(role.getName().substring(5))
                .description(role.getDescription())
                .permissions(role.getPermissions().stream().map(this::convertPermission).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PermissionDto convertPermission(Permission permission) {
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setId(permission.getId());
        permissionDto.setName(permission.getName());
        return permissionDto;
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    private Permission findPermissionById(PermissionDto permissionDto) {
        return permissionRepository.findById(permissionDto.getId()).orElse(null);
    }

}
