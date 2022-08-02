package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.service.ScreenPermissionService;
import com.pentyugov.wflow.core.service.ScreenService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

@RestController
@RequestMapping("/api/screen-permissions")
public class ScreenController extends ExceptionHandling {

    private final ScreenPermissionService screenPermissionService;
    private final ScreenService screenService;
    private final UserService userService;

    public ScreenController(ScreenPermissionService screenPermissionService, ScreenService screenService, UserService userService) {
        this.screenPermissionService = screenPermissionService;
        this.screenService = screenService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get screen permissions for current user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getScreenPermissionsForCurrentUser(Principal principal) throws UserNotFoundException {
        return new ResponseEntity<>(this.screenPermissionService.loadScreenPermissionForCurrentUser(principal), HttpStatus.OK);
    }

    @GetMapping("/{screenId}")
    @Operation(summary = "Checks is user have access to screen", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> hasScreenAccess(@PathVariable String screenId, Principal principal) throws UserNotFoundException {
        ResponseEntity<Object> responseEntity;
        if (userService.isUserAdmin(userService.getUserByPrincipal(principal))) {
            responseEntity = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(screenService.hasAccessToScreen(principal, screenId), HttpStatus.OK);
        }
        return responseEntity;
    }
}
