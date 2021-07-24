package com.codegrade.restapi.entity;

import com.codegrade.restapi.security.UserRole;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.usertype.UserType;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
public class UserAccount  {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;

    private String username;
    private String email;
    private String password;

    private UserRole role;
    private Boolean isVerified;
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

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", isVerified=" + isVerified +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
