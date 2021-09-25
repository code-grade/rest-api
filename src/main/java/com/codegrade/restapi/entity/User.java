package com.codegrade.restapi.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity(name = "user_account")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;

    @NotBlank(message = "username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "password is required")
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "email")
    private @Valid Email email;

    @NotBlank(message = "firstName is required")
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName = "";

    @ManyToOne
    @JoinColumn(name = "role")
    private @Valid UserRole role;

    @Column(nullable = false)
    private Boolean isEnabled = true;

    public User(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        if (this == o) return true;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

    @Getter @AllArgsConstructor
    public static class UserWithoutPass {
        private UUID userId;
        private String username;
        private Email email;
        private String firstName;
        private String lastName;
        private String role;
        private Boolean isEnabled;

        public static User.UserWithoutPass fromUser(User ua) {
          return new User.UserWithoutPass(
                  ua.userId,
                  ua.username,
                  ua.email,
                  ua.firstName,
                  ua.lastName,
                  ua.role.getRole(),
                  ua.isEnabled
          );
        }
    }

}
