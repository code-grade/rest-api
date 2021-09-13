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

@Component
@Getter
@Setter
public class JwtUtils {

    @Data
    @AllArgsConstructor
    public static class JwtData {
        private String username;
        private List<String> roles;
    }

    private final JwtConfig config;

    public JwtUtils(JwtConfig config) {
        this.config = config;
    }

    public String signJwt(String username, List<String> roles) {

        LocalDate currentDate = LocalDate.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(java.sql.Date.valueOf(currentDate))
                .setExpiration(java.sql.Date.valueOf(currentDate.plusDays(config.getExpiresIn())))
                .signWith(config.getSecretKeyAsKey())
                .compact();
    }

    public JwtData parseJwt(String token) {
        try {
            Jws<Claims> parsedClaims = Jwts.parser().setSigningKey(config.getSecretKeyAsKey()).parseClaimsJws(token);
            return new JwtData(
                    parsedClaims.getBody().getSubject(),
                    (List<String>) parsedClaims.getBody().get("roles")
            );
        } catch (JwtException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("expired or corrupted authorization token"));
        } catch (RuntimeException ex) {
            throw new ApiException(RBuilder.badRequest().setMsg("invalid authorization token"));
        }
    }
}
