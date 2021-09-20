package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.util.Objects;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRole {

    public static final UserRole ADMIN =
            new UserRole("ADMIN", "An user with administrator privileges");
    public static final UserRole INSTRUCTOR =
            new UserRole("INSTRUCTOR", "An user who can coordinate students");
    public static final UserRole STUDENT =
            new UserRole("STUDENT", "An user who can participate for assignment");

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";

    @Id
    private String role;
    private String description;

    public UserRole(String role) {
        this.role = role;
    }

    public String getRoleFullName() {
        return "ROLE_" + role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == String.class) {
            return o.equals(getRoleFullName());
        }
        if (getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return role.equals(userRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
