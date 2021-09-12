package com.codegrade.restapi.config;

import com.codegrade.restapi.exception.RestAuthenticationFailureHandler;
import com.codegrade.restapi.filter.JWTAuthenticationFilter;
import com.codegrade.restapi.filter.JWTAuthorizationFilter;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.BPEncoder;
import com.codegrade.restapi.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final BPEncoder bpEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JWTAuthenticationFilter authenticator = new JWTAuthenticationFilter(
                authenticationManager(), jwtUtils,
                authenticationFailureHandler()
        );
        JWTAuthorizationFilter authorizer = new JWTAuthorizationFilter(jwtUtils.getConfig(), jwtUtils);

        http
                // CSRF Certificate & Session
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Filters
                .addFilter(authenticator)
                .addFilterAfter(authorizer, JWTAuthenticationFilter.class)

                // Authorization
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/user").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) {
        authBuilder.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bpEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }
}