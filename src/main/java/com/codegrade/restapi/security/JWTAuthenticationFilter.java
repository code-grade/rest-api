package com.codegrade.restapi.security;

import com.codegrade.restapi.entity.AuthenticationRequest;
import com.codegrade.restapi.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter @Setter
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils,
                                   AuthenticationFailureHandler authenticationFailureHandler) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        String token = jwtUtils.signJwt(
                auth.getName(),
                ImmutableMap.of("authorities", auth.getAuthorities())
        );

        response.resetBuffer();
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(
                ImmutableMap.of("token", token)
        ));

        response.flushBuffer(); // marks response as committed
    }


}
