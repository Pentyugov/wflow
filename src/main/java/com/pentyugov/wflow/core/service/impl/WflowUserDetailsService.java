package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.repository.UserRepository;
import com.pentyugov.wflow.core.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("UserDetailsService")
@RequiredArgsConstructor
public class WflowUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username: " + username + " not found"));
        validateLoginAttempt(user);
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(LocalDateTime.now());
        user = userRepository.save(user);
        return build(user);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    private static User build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.addAll(
                    role.getPermissions().stream().map(permission ->
                            new SimpleGrantedAuthority(permission.getName())).collect(Collectors.toList())
            );
        }

        return new User(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getLastLoginDate(),
                user.getLastLoginDateDisplay(),
                user.getJoinDate(),
                user.getRoles(),
                authorities,
                user.getVersion(),
                user.isActive(),
                user.isNotLocked());
    }


}
