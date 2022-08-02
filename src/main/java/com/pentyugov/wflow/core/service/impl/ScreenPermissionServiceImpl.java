package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.ScreenPermissions;
import com.pentyugov.wflow.core.dto.ScreenPermissionDto;
import com.pentyugov.wflow.core.repository.ScreenPermissionRepository;
import com.pentyugov.wflow.core.service.ScreenPermissionService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(ScreenPermissionService.NAME)
@RequiredArgsConstructor
public class ScreenPermissionServiceImpl implements ScreenPermissionService {

    private final UserService userService;
    private final ScreenPermissionRepository screenPermissionRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ScreenPermissionDto> loadScreenPermissionForCurrentUser(Principal principal) throws UserNotFoundException {
        return screenPermissionRepository
                .findAllByRoles(getRolesIds(principal))
                .stream()
                .map(this::createScreenPermissionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScreenPermissionDto> loadScreenPermissionForCurrentUser(Principal principal, String screenId) throws UserNotFoundException {
        return screenPermissionRepository
                .findScreenByRoles(getRolesIds(principal), screenId)
                .stream()
                .map(this::createScreenPermissionDto)
                .collect(Collectors.toList());

    }

    private ScreenPermissionDto createScreenPermissionDto(ScreenPermissions screenPermissions) {
        return modelMapper.map(screenPermissions, ScreenPermissionDto.class);
    }

    private List<UUID> getRolesIds(Principal principal) throws UserNotFoundException {
        return userService.getUserByPrincipal(principal)
                .getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
