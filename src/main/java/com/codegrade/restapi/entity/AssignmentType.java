package com.codegrade.restapi.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class AssignmentType {

    public static String T_PUBLIC = "PUBLIC";
    public static String T_PRIVATE = "PRIVATE";

    public static AssignmentType PUBLIC = new AssignmentType("PUBLIC");
    public static AssignmentType PRIVATE = new AssignmentType("PRIVATE");

    @Id
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentType that = (AssignmentType) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
