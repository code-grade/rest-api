package com.codegrade.restapi.filter;

import com.codegrade.restapi.utils.JwtUtils;
import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Getter
@Setter
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Data
    public static class AuthRequest {
        private String username;
        private String password;
    }

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
            AuthRequest authReq = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authReq.getUsername(),
                    authReq.getPassword()
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
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),
                RBuilder.success()
                        .setData("user", auth.getName())
                        .setToken(token).compact()
        );

    }


}
