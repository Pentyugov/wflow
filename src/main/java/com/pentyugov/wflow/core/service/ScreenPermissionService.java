package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.dto.ScreenPermissionDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;

import java.security.Principal;
import java.util.List;

public interface ScreenPermissionService {
    String NAME = "wflow$ScreenPermissionService";
    List<ScreenPermissionDto> loadScreenPermissionForCurrentUser(Principal principal) throws UserNotFoundException;
    List<ScreenPermissionDto> loadScreenPermissionForCurrentUser(Principal principal, String screenId) throws UserNotFoundException;
}
