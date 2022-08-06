package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;


public interface UserSettingsService {

    String NAME = "wflow$UserSettingsService";
    
    UserSettings getUserSettings(User user);

    UserSettings createDefaultSettingsForUser(User user);

    UserSettings saveUserSettings(UserSettingsDto userSettingsDto);

    void updateUserSettings(UserSettings userSettings);

    UserSettingsDto createProxyFromUserSettings(UserSettings userSettings);

    UserSettings createUserSettingsFromProxy(UserSettingsDto userSettingsDto);

}
