package com.codegrade.restapi.filter;

import com.codegrade.restapi.config.JwtConfig;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.utils.JwtUtils;
import com.codegrade.restapi.utils.RBuilder;
import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter @Setter
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            throw new ApiException(RBuilder.badRequest().setMsg("incorrect authorization header format"));
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim();

        try {
            var jwtData = jwtUtils.parseJwt(token);
            var simpleGrantedAuthorities = jwtData.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            var authentication = new UsernamePasswordAuthenticationToken(
                    jwtData.getUsername(),
                    null,
                    simpleGrantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);

    }
}
