package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.core.service.UserSettingsService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class ApplicationController extends AbstractController {

    private final UserSessionService userSessionService;
    private final UserSettingsService userSettingsService;

    @GetMapping("/user-settings/get-user-settings")
    public ResponseEntity<Object> getUserSettings() {
        UserSettings userSettings = userSettingsService.getUserSettings(userSessionService.getCurrentUser());
        return new ResponseEntity<>(userSettingsService.createProxyFromUserSettings(userSettings), HttpStatus.OK);
    }

    @PostMapping("/user-settings/save-user-settings")
    public ResponseEntity<Object> saveUserSettings(@RequestBody UserSettingsDto userSettingsDto) {
        UserSettings userSettings = userSettingsService.saveUserSettings(userSettingsDto);
        return new ResponseEntity<>(userSettingsService.createProxyFromUserSettings(userSettings), HttpStatus.OK);
    }

    @PostMapping("/user-settings/change-widget-settings")
    public ResponseEntity<Object> changeWidgetSettings(@RequestBody UserSettingsDto userSettingsDto) {
        UserSettings userSettings = userSettingsService.saveUserSettings(userSettingsDto);
        return new ResponseEntity<>(userSettingsService.createProxyFromUserSettings(userSettings), HttpStatus.OK);
    }

}
