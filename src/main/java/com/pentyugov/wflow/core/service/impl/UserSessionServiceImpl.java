package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service(UserSessionService.NAME)
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Set<Role> getUserRoles() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRoles();
    }

    @Override
    public Boolean isUserInRole(String roleName) {
        return getCurrentUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(roleName));

    }

    @Override
    public Boolean isUserInAnyRole(List<String> roleNames) {
        for (String roleName : roleNames) {
            if (isUserInRole(roleName)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public Boolean isCurrentUserAdmin() {
        return getCurrentUser()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(Role.ADMIN));
    }
}
