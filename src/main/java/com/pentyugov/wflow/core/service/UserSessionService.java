package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;

import java.util.List;
import java.util.Set;

public interface UserSessionService {

    String NAME = "wflow$UserSessionService";

    User getCurrentUser();
    Set<Role> getUserRoles();
    Boolean isUserInRole(String roleName);
    Boolean isUserInAnyRole(List<String> roleNames);
    Boolean isCurrentUserAdmin();

}
