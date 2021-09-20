package com.codegrade.restapi.utils;

import com.codegrade.restapi.config.JwtConfig;
import com.codegrade.restapi.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig config;

    public String signAuthJwt(String username, UUID userId, List<String> roles) {

        LocalDate currentDate = LocalDate.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("userId", userId)
                .setIssuedAt(java.sql.Date.valueOf(currentDate))
                .setExpiration(java.sql.Date.valueOf(currentDate.plusDays(config.getExpiresIn())))
                .signWith(config.getSecretKeyAsKey())
                .compact();
    }

    public AuthJwtData parseAuthJwt(String token) {
        try {
            Jws<Claims> parsedClaims = Jwts.parser().setSigningKey(config.getSecretKeyAsKey()).parseClaimsJws(token);
            return new AuthJwtData(
                    parsedClaims.getBody().getSubject(),
                    (String) parsedClaims.getBody().get("userId"),
                    (List<String>) parsedClaims.getBody().get("roles")
            );
        } catch (JwtException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("expired or corrupted authorization token"));
        } catch (RuntimeException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("invalid authorization token"));
        }
    }

    public String signEmailJwt(String username, String email) {
        LocalDate currentDate = LocalDate.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .setIssuedAt(java.sql.Date.valueOf(currentDate))
                .setExpiration(java.sql.Date.valueOf(currentDate.plusDays(1)))
                .signWith(config.getSecretKeyAsKey())
                .compact();
    }

    public EmailJwtData parseEmailJwt(String token) {
        try {
            Jws<Claims> parsedClaims = Jwts.parser().setSigningKey(config.getSecretKeyAsKey()).parseClaimsJws(token);
            return new EmailJwtData(
                    parsedClaims.getBody().getSubject(),
                    (String) parsedClaims.getBody().get("email")
            );
        } catch (JwtException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("expired or corrupted authorization token"));
        } catch (RuntimeException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("invalid authorization token"));
        }
    }

    @Data
    @AllArgsConstructor
    public static class AuthJwtData {
        private String username;
        private String userId;
        private List<String> roles;
    }

    @Data
    @AllArgsConstructor
    public static class EmailJwtData {
        private String username;
        private String email;
    }
}
