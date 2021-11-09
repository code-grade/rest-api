package com.codegrade.restapi.config;

import com.codegrade.restapi.entity.Email;
import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ConfigurationProperties("auth.super-user")
public class SuperUserConfig {

    private Boolean enable = false;
    private String username = "admin";
    private String password = "admin";
    private String firstName = "Admin";
    private String lastName = "User";
    private String email = "admin@code-grade.com";

    public User getUserAccount() {
        return new User(
                null, username, password, new Email(email),
                firstName, lastName,
                UserRole.ADMIN, true
        );
    }
}
