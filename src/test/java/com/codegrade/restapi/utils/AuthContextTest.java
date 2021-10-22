package com.codegrade.restapi.utils;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class AuthContextTest {

    UUID mockAuthentication(String role) {
        UUID userId = UUID.randomUUID();
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(userId.toString());
        Mockito.when(authentication.getAuthorities())
                .thenReturn((Collection) Set.of(new SimpleGrantedAuthority(role)));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        return userId;
    }

    @Test
    void fromContextHolder() {
        mockAuthentication(UserRole.ROLE_ADMIN);
        AuthContext context = AuthContext.fromContextHolder();
        assertNotNull(context);
        MatcherAssert.assertThat(context, instanceOf(AuthContext.class));
    }

    @Test
    void getUserId() {
        UUID userId = mockAuthentication(UserRole.ROLE_ADMIN);
        AuthContext context = AuthContext.fromContextHolder();
        assertEquals(userId, context.getUserId());
    }

    @Test
    void getUser() {
        UUID userId = mockAuthentication(UserRole.ROLE_ADMIN);
        AuthContext context = AuthContext.fromContextHolder();
        assertEquals(new User(userId), context.getUser());
    }

    @Test
    void isAdmin() {
        mockAuthentication(UserRole.ROLE_ADMIN);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.isAdmin());
    }

    @Test
    void isInstructor() {
        mockAuthentication(UserRole.ROLE_INSTRUCTOR);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.isInstructor());
    }

    @Test
    void isStudent() {
        mockAuthentication(UserRole.ROLE_STUDENT);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.isStudent());
    }

    @Test
    void matchUserIdOptional() {
        UUID userId = mockAuthentication(UserRole.ROLE_STUDENT);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.matchUserId(Optional.of(userId.toString())));
        assertFalse(context.matchUserId(Optional.of(UUID.randomUUID().toString())));
    }

    @Test
    void matchUserIdUUID() {
        UUID userId = mockAuthentication(UserRole.ROLE_STUDENT);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.matchUserId(userId));
        assertFalse(context.matchUserId(UUID.randomUUID()));
    }

    @Test
    void matchUserIdString() {
        UUID userId = mockAuthentication(UserRole.ROLE_STUDENT);
        AuthContext context = AuthContext.fromContextHolder();
        assertTrue(context.matchUserId(userId.toString()));
        assertFalse(context.matchUserId(UUID.randomUUID().toString()));
    }
}