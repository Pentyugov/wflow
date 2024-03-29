package com.pentyugov.wflow.core.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "security$User")
@Table(name = "SECURITY_USERS")
@Where(clause = "DELETE_DATE is null")
public class User extends BaseEntity implements UserDetails {

    public User(UUID id,
                String username,
                String password,
                String firstName,
                String lastName,
                String email,
                String profileImageUrl,
                LocalDateTime lastLoginDate,
                LocalDateTime lastLoginDateDisplay,
                LocalDateTime joinDate,
                Set<Role> roles,
                Collection<GrantedAuthority> authorities,
                Integer version,
                boolean isActive,
                boolean isNotLocked) {
        this.setId(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.roles = roles;
        this.authorities = authorities;
        this.setVersion(version);
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
    }

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 3000)
    private String password;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "IMAGE_URL")
    private String profileImageUrl;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

    @Column(name = "LAST_LOGIN_DATE_DISPLAY")
    private LocalDateTime lastLoginDateDisplay;

    @Column(name = "JOIN_DATE")
    private LocalDateTime joinDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "security_user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    @Column(name = "IS_NOT_LOCKED", nullable = false)
    private boolean isNotLocked;

    @Column(name = "TEL_USER_ID")
    private Long telUserId;

    @Column(name = "TEL_CHAT_ID")
    private Long telChatId;

    @Column(name = "TEL_LOGGED")
    private Boolean telLogged = Boolean.FALSE;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @PrePersist
    public void onJoin() {
        this.joinDate = LocalDateTime.now();
    }

    public String getFullName() {
        if (this.firstName != null && this.lastName != null) {
            return this.firstName + " " + this.lastName;
        }
        return this.username;
    }
}
