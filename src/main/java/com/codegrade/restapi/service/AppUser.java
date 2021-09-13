package com.codegrade.restapi.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

@Getter @Setter
public class AppUser extends User {

    private UUID userId;
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public AppUser(String username, String password,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = null;
        this.password = password;
        this.username = username;
        this.authorities =  new HashSet<>(authorities);;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public AppUser(UUID userId, String username, String password, boolean enabled, boolean accountNonLocked,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true,
                accountNonLocked, authorities);
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.authorities = new HashSet<>(authorities);
        this.accountNonExpired = true;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = true;
        this.enabled = enabled;
    }

    public static AppUserBuilder appUserBuilder() {
        return new AppUserBuilder();
    }

    public static final class AppUserBuilder {
        private UUID userId;
        private String username;
        private String password;
        private List<GrantedAuthority> authorities;
        private boolean accountLocked;
        private boolean disabled;
        private Function<String, String> passwordEncoder;

        private AppUserBuilder() {
            this.passwordEncoder = (password) -> {
                return password;
            };
        }

        public AppUserBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }


        public AppUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public AppUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public AppUserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public AppUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            int var4 = roles.length;

            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> {
                    return role + " cannot start with ROLE_ (it is automatically added)";
                });
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return this.authorities(authorities);
        }

        public AppUserBuilder authorities(GrantedAuthority... authorities) {
            return this.authorities(Arrays.asList(authorities));
        }

        public AppUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public AppUserBuilder authorities(String... authorities) {
            return this.authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public AppUserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public AppUserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserDetails build() {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return new AppUser(this.userId, this.username, encodedPassword, !this.disabled, !this.accountLocked, this.authorities);
        }
    }

}
