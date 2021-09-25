package com.codegrade.restapi.utils;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthContext {

    private final Authentication auth;
    private String userRole;

    public static AuthContext fromContextHolder() {
        return new AuthContext(SecurityContextHolder.getContext().getAuthentication());
    }

    public AuthContext(Authentication auth) {
        this.auth = auth;
        var roles = this.auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if (!roles.isEmpty()) {
            this.userRole = roles.get(0);
        }
    }

    public UUID getUserId() {
        return UUID.fromString(this.auth.getName());
    }

    public User getUser() {
        var user = new User(getUserId());
        user.setRole(new UserRole(this.userRole));
       return new User();
    }

    public boolean is(String role) {
        return role.equals(this.userRole);
    }

    public boolean isAdmin() {
        return is(UserRole.ROLE_ADMIN);
    }

    public boolean isInstructor() {
        return is(UserRole.ROLE_INSTRUCTOR);
    }

    public boolean isStudent() {
        return is(UserRole.ROLE_STUDENT);
    }

    public boolean matchesUserId(Optional<String> userId) {
        return userId.map(s -> s.equals(this.auth.getName())).orElse(false);
    }

    public boolean matchUserId(String userId) {
        return userId.equals(this.auth.getName());
    }

}
