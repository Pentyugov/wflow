package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;

import java.security.Principal;


public interface UserSettingsService {

    String NAME = "wflow$UserSettingsService";
    
    UserSettings getUserSettings(User user);

    UserSettings createDefaultSettingsForUser(User user);

    UserSettings saveUserSettings(UserSettingsDto userSettingsDto);

    UserSettingsDto createProxyFromUserSettings(UserSettings userSettings);

    UserSettings createUserSettingsFromProxy(UserSettingsDto userSettingsDto);

}
