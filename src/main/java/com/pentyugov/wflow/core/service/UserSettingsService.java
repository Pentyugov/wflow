package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;

import java.security.Principal;
import java.util.Locale;


public interface UserSettingsService {

    String NAME = "wflow$UserSettingsService";
    
    UserSettings getUserSettings(User user);

    UserSettings createDefaultSettingsForUser(User user);

    UserSettings saveUserSettings(UserSettings userSettings);

    UserSettings saveUserSettings(UserSettingsDto userSettingsDto, Principal principal) throws UserNotFoundException;

    UserSettingsDto createProxyFromUserSettings(UserSettings userSettings);

    UserSettings createUserSettingsFromProxy(UserSettingsDto userSettingsDto);

    Locale getLocale(Principal principal) throws UserNotFoundException;


}
