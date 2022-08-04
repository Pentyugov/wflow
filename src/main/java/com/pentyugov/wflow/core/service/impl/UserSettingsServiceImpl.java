package com.pentyugov.wflow.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;
import com.pentyugov.wflow.core.dto.WidgetSettingsDto;
import com.pentyugov.wflow.core.repository.UserSettingsRepository;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.core.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service(UserSettingsService.NAME)
@RequiredArgsConstructor
public class UserSettingsServiceImpl extends AbstractService implements UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserSessionService userSessionService;

    @Override
    public UserSettings getUserSettings(User user) {
        UserSettings userSettings = userSettingsRepository.findUserSettingsForUser(user.getId()).orElse(null);
        if (ObjectUtils.isEmpty(userSettings)) {
            return createDefaultSettingsForUser(user);
        }
        return userSettings;
    }

    @Override
    public UserSettings createDefaultSettingsForUser(User user) {
        UserSettings userSettings = new UserSettings();
        userSettings.setUser(user);
        userSettings.setLocale("en");
        userSettings.setEnableChatNotificationSound(true);
        userSettings.setMiniSidebar(false);
        userSettings.setThemeColor(10);
        userSettings.setDarkTheme(false);
        userSettings.setWidgetSettings(getDefaultWidgetSettings());
        return userSettingsRepository.save(userSettings);
    }

    @Override
    public UserSettings saveUserSettings(UserSettingsDto userSettingsDto) {
        User user = userSessionService.getCurrentUser();
        UserSettings userSettings = createUserSettingsFromProxy(userSettingsDto);
        userSettings.setUser(user);
        return userSettingsRepository.save(userSettings);
    }

    @Override
    public UserSettingsDto createProxyFromUserSettings(UserSettings userSettings) {
        ObjectMapper mapper = new ObjectMapper();
        UserSettingsDto userSettingsDto = new UserSettingsDto();
        try {
            List<WidgetSettingsDto> widgetSettings = mapper.readValue(userSettings.getWidgetSettings(), new TypeReference<>() {
            });
            userSettingsDto.setWidgetSettings(widgetSettings);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        userSettingsDto.setId(userSettings.getId());
        userSettingsDto.setLocale(userSettings.getLocale());
        userSettingsDto.setEnableChatNotificationSound(userSettings.getEnableChatNotificationSound());
        userSettingsDto.setThemeColor(userSettings.getThemeColor());
        userSettingsDto.setMiniSidebar(userSettings.getMiniSidebar());
        userSettingsDto.setDarkTheme(userSettings.getDarkTheme());

        return userSettingsDto;
    }

    @Override
    public UserSettings createUserSettingsFromProxy(UserSettingsDto userSettingsDto) {

        UserSettings userSettings = null;
        if (!ObjectUtils.isEmpty(userSettingsDto.getId())) {
            userSettings = userSettingsRepository.getById(userSettingsDto.getId());
        }

        if (ObjectUtils.isEmpty(userSettings)) {
            userSettings = new UserSettings();
            userSettings.setId(userSettingsDto.getId());
        }

        userSettings.setWidgetSettings(getWidgetSettingsAsString(userSettingsDto.getWidgetSettings()));
        userSettings.setLocale(userSettingsDto.getLocale());
        userSettings.setEnableChatNotificationSound(userSettingsDto.getEnableChatNotificationSound());
        userSettings.setThemeColor(userSettingsDto.getThemeColor());
        userSettings.setMiniSidebar(userSettingsDto.getMiniSidebar());
        userSettings.setDarkTheme(userSettingsDto.getDarkTheme());
        return userSettings;
    }

    private String getDefaultWidgetSettings() {
        return getWidgetSettingsAsString(null);
    }

    private String getWidgetSettingsAsString(List<WidgetSettingsDto> widgetSettings) {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String result;

        try {
            if (widgetSettings != null) {
                result = objectWriter.writeValueAsString(widgetSettings);
            } else {
                result = objectWriter.writeValueAsString(WidgetSettingsDto.createDefaultWidgetSettings());
            }
        } catch (JsonProcessingException e) {
            result = null;
        }

        return result;
    }
}
