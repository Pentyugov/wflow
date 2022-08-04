package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.dto.ScreenPermissionDto;
import com.pentyugov.wflow.core.service.ScreenPermissionService;
import com.pentyugov.wflow.core.service.ScreenService;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Screen.Action.*;

@Service(ScreenService.NAME)
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenPermissionService screenPermissionService;

    @Override
    public boolean hasAccessToScreen(String screenAlias) {
        if (!StringUtils.hasText(screenAlias))
            return false;

        String[] tmp = screenAlias.split("\\.");
        String screenId = tmp[0];
        List<ScreenPermissionDto> screenPermissions = screenPermissionService.loadScreenPermissionForCurrentUser(screenId);


        if (CollectionUtils.isEmpty(screenPermissions))
            return false;

        if (tmp.length > 1) {
            String subScreen = screenAlias.split("\\.")[1];
            if (StringUtils.hasText(subScreen)) {
                switch (subScreen) {
                    case SCREEN_ACTION_BROWSE: return screenPermissions.stream().anyMatch(screenPermissionDto -> screenPermissionDto.getRead()   == Boolean.TRUE);
                    case SCREEN_ACTION_CREATE: return screenPermissions.stream().anyMatch(screenPermissionDto -> screenPermissionDto.getCreate() == Boolean.TRUE);
                    case SCREEN_ACTION_EDIT:   return screenPermissions.stream().anyMatch(screenPermissionDto -> screenPermissionDto.getUpdate() == Boolean.TRUE);
                    case SCREEN_ACTION_DELETE: return screenPermissions.stream().anyMatch(screenPermissionDto -> screenPermissionDto.getDelete() == Boolean.TRUE);
                    default: return false;
                }
            }
        }

        return false;
    }



}
