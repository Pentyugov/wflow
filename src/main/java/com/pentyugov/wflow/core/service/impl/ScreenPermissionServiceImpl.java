package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.ScreenPermissions;
import com.pentyugov.wflow.core.dto.ScreenPermissionDto;
import com.pentyugov.wflow.core.repository.ScreenPermissionRepository;
import com.pentyugov.wflow.core.service.ScreenPermissionService;
import com.pentyugov.wflow.core.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(ScreenPermissionService.NAME)
@RequiredArgsConstructor
public class ScreenPermissionServiceImpl implements ScreenPermissionService {

    private final ScreenPermissionRepository screenPermissionRepository;
    private final ModelMapper modelMapper;
    private final UserSessionService userSessionService;

    @Override
    public List<ScreenPermissionDto> loadScreenPermissionForCurrentUser() {
        return screenPermissionRepository
                .findAllByRoles(getRolesIds())
                .stream()
                .map(this::createScreenPermissionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScreenPermissionDto> loadScreenPermissionForCurrentUser(String screenId) {
        return screenPermissionRepository
                .findScreenByRoles(getRolesIds(), screenId)
                .stream()
                .map(this::createScreenPermissionDto)
                .collect(Collectors.toList());

    }

    private ScreenPermissionDto createScreenPermissionDto(ScreenPermissions screenPermissions) {
        return modelMapper.map(screenPermissions, ScreenPermissionDto.class);
    }

    private List<UUID> getRolesIds() {
        return userSessionService.getCurrentUser()
                .getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
