package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.web.exception.UserNotFoundException;

import java.security.Principal;

public interface ScreenService {
    String NAME = "wflow$ScreenService";

    boolean hasAccessToScreen(Principal principal, String screenAlias) throws UserNotFoundException;
}
