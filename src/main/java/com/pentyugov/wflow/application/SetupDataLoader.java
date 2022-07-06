package com.pentyugov.wflow.application;

import com.pentyugov.wflow.core.domain.entity.Permission;
import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.repository.PermissionRepository;
import com.pentyugov.wflow.core.repository.RoleRepository;
import com.pentyugov.wflow.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Permission readPermission = createPermissionIfNotFound(Permission.READ);
        Permission updatePermission = createPermissionIfNotFound(Permission.UPDATE);
        Permission createPermission = createPermissionIfNotFound(Permission.CREATE);
        Permission deletePermission = createPermissionIfNotFound(Permission.DELETE);
        Permission sendSysMailPermission = createPermissionIfNotFound(Permission.SEND_SYS_MAIL);

        List<Permission> adminPermissions = Arrays.asList(readPermission, createPermission, deletePermission, updatePermission, sendSysMailPermission);
        List<Permission> userPermissions = Arrays.asList(readPermission);

        createRoleIfNotFound(Role.ADMIN, adminPermissions);
        createRoleIfNotFound(Role.USER, userPermissions);

        Role adminRole = roleRepository.findByName(Role.ADMIN).orElse(null);
        Role userRole = roleRepository.findByName(Role.USER).orElse(null);


        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setEmail("wflow@internet.ru");
        if (!ObjectUtils.isEmpty(adminRole)) {
            admin.setRoles(Set.of(adminRole));
        }
        admin.setActive(true);
        admin.setNotLocked(true);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("wflow-user@internet.ru");
        if (!ObjectUtils.isEmpty(userRole)) {
            user.setRoles(Set.of(userRole));
        }
        user.setActive(true);
        user.setNotLocked(true);

        userRepository.save(user);
        userRepository.delete(user);

        alreadySetup = true;
    }

    @Transactional
    Permission createPermissionIfNotFound(String name) {
        Permission permission = permissionRepository.findByName(name).orElse(null);
        if (permission == null) {
            permission = new Permission(name);
            permissionRepository.save(permission);
        }
        return permission;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Permission> permissions) {
        Role role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            role = new Role(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
        return role;
    }
}