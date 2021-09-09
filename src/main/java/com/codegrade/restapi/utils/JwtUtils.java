package com.codegrade.restapi.utils;

import com.codegrade.restapi.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@Getter
@Setter
public class JwtUtils {

    private final JwtConfig config;

    public JwtUtils(JwtConfig config) {
        this.config = config;
    }

    public String signJwt(String subject, Map<String, Object> payload) {
        LocalDate currentDate = LocalDate.now();
        return Jwts.builder().setSubject(subject).setClaims(payload)
                .setIssuedAt(java.sql.Date.valueOf(currentDate))
                .setExpiration(java.sql.Date.valueOf(currentDate.plusDays(config.getExpiresIn())))
                .signWith(config.getSecretKeyAsKey())
                .compact();
    }


    public Jws<Claims> parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(config.getSecretKeyAsKey())
                .parseClaimsJws(token);
    }
}
