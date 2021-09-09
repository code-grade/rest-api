package com.codegrade.restapi.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserAccount  {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;

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
