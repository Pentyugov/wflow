package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.service.ScreenPermissionService;
import com.pentyugov.wflow.core.service.ScreenService;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

@RestController
@RequestMapping("/api/screen-permissions")
@RequiredArgsConstructor
public class ScreenController extends ExceptionHandling {

    private final ScreenPermissionService screenPermissionService;
    private final ScreenService screenService;
    private final UserSessionService userSessionService;

    @GetMapping
    @Operation(summary = "Get screen permissions for current user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getScreenPermissionsForCurrentUser() {
        return new ResponseEntity<>(this.screenPermissionService.loadScreenPermissionForCurrentUser(), HttpStatus.OK);
    }

    @GetMapping("/{screenId}")
    @Operation(summary = "Checks is user have access to screen", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> hasScreenAccess(@PathVariable String screenId) {
        ResponseEntity<Object> responseEntity;
        if (userSessionService.isCurrentUserAdmin()) {
            responseEntity = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(screenService.hasAccessToScreen(screenId), HttpStatus.OK);
        }
        return responseEntity;
    }
}
