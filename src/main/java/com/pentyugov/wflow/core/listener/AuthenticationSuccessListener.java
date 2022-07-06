package com.pentyugov.wflow.core.listener;

import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
