package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.UserSettingsDto;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.core.service.UserSettingsService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/app")
public class ApplicationController extends AbstractController {

    private final UserService userService;
    private final UserSettingsService userSettingsService;

    public ApplicationController(UserService userService, UserSettingsService userSettingsService) {
        this.userService = userService;
        this.userSettingsService = userSettingsService;
    }

    @GetMapping("/user-settings/get-user-settings")
    public ResponseEntity<Object> getUserSettings(Principal principal) throws UserNotFoundException {
        UserSettings userSettings = userSettingsService.getUserSettings(userService.getUserByPrincipal(principal));
        return new ResponseEntity<>(userSettingsService.createProxyFromUserSettings(userSettings), HttpStatus.OK);
    }

    @PostMapping("/user-settings/save-user-settings")
    public ResponseEntity<Object> saveUserSettings(@RequestBody UserSettingsDto userSettingsDto, Principal principal) throws UserNotFoundException {
        UserSettings userSettings = userSettingsService.saveUserSettings(userSettingsDto, principal);
        return new ResponseEntity<>(userSettingsService.createProxyFromUserSettings(userSettings), HttpStatus.OK);
    }

}
