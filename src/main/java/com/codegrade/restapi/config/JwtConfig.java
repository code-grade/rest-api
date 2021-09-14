package com.codegrade.restapi.config;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ConfigurationProperties("auth.jwt")
public class JwtConfig {

    private String secretKey;
    private int expiresIn;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public String getTokenPrefix() {
        return "Bearer";
    }

    public SecretKey getSecretKeyAsKey() {
        return Keys.hmacShaKeyFor((this.getSecretKey()).getBytes());
    }

}
