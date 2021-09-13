package com.codegrade.restapi.config;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("auth.super-user")
public class SuperUserConfig {
    private String username = "admin";
    private String password = "admin";
    private String firstName = "Admin";
    private String lastName = "User";
    private String email = "admin@code-grade.com";

    public UserAccount getUserAccount() {
        return new UserAccount(
                null, username, password, email,
                firstName, lastName,
                UserRole.ADMIN, true, true
        );
    }
}
