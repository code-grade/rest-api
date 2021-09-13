package com.codegrade.restapi.entity;

import com.codegrade.restapi.utils.EnumConstraint;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UserAccount  {

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

    @NotBlank(message = "email is required")
    @Email(message = "invalid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "firstName is required")
    @Column(nullable = false)
    private String firstName;

    @Column(columnDefinition = "VARCHAR(100) default ''")
    private String lastName;

    @EnumConstraint(enumClass = UserRole.class, message = "invalid user role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false, columnDefinition = "VARCHAR(100) default false")
    private Boolean isVerified;

    @Column(nullable = false, columnDefinition = "VARCHAR(100) default true")
    private Boolean isEnabled;

    @Override
    public boolean equals(Object o) {
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        if (this == o) return true;
        UserAccount userAccount = (UserAccount) o;
        return Objects.equals(userId, userAccount.userId);
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

    @Getter @AllArgsConstructor
    public static class UserWithoutPass {
        private UUID userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private UserRole role;
        private Boolean isVerified;
        private Boolean isEnabled;

        public static UserAccount.UserWithoutPass fromUserAccount(UserAccount ua) {
          return new UserAccount.UserWithoutPass(
                  ua.userId,
                  ua.username,
                  ua.email,
                  ua.firstName,
                  ua.lastName,
                  ua.role,
                  ua.isVerified,
                  ua.isEnabled
          );
        };
    }

}
