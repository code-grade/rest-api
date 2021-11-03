package com.codegrade.restapi.utils;

import com.codegrade.restapi.config.JwtConfig;
import com.codegrade.restapi.entity.UserRole;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
class JwtUtilsTest {

    public JwtConfig mockConfig() {
        return new JwtConfig("thisisthetestingsecretthisisthetestingsecretthisisthetestingsecret", 1);
    }

    @Test
    void signAuthJwt() {
        JwtUtils jwtUtils = new JwtUtils(mockConfig());
        var token = jwtUtils.signAuthJwt("username", UUID.randomUUID(), List.of(UserRole.ROLE_STUDENT));
        Assertions.assertTrue(Jwts.parser().isSigned(token));
    }

    @Test
    void parseAuthJwt() {
        var jwtUtils = new JwtUtils(mockConfig());
        var username = "username";
        var userId = UUID.randomUUID();
        var roles = List.of(UserRole.ROLE_STUDENT);

        // sign token
        var token = jwtUtils.signAuthJwt(username, userId, roles);

        // parse
        try {
            var payload = jwtUtils.parseAuthJwt(token);
            Assertions.assertEquals(payload.getUsername(), username);
            Assertions.assertEquals(payload.getUserId(), (userId.toString()));
            Assertions.assertEquals(payload.getRoles(), roles);
        } catch (JwtException e) {
            Assertions.fail();
        }
    }

    @Test
    void signEmailJwt() {
        JwtUtils jwtUtils = new JwtUtils(mockConfig());
        var token = jwtUtils.signEmailJwt("username", "email@email.com");
        Assertions.assertTrue(Jwts.parser().isSigned(token));
    }

    @Test
    void parseEmailJwt() {
        var jwtUtils = new JwtUtils(mockConfig());
        var username = "username";
        var email = "info@code-grade.com";

        // sign token
        var token = jwtUtils.signEmailJwt(username, email);

        // parse
        try {
            var payload = jwtUtils.parseEmailJwt(token);
            Assertions.assertEquals(payload.getUsername(), username);
            Assertions.assertEquals(payload.getEmail(),email);
        } catch (JwtException e) {
            Assertions.fail();
        }
    }

}