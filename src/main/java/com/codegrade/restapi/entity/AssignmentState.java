package com.codegrade.restapi.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AssignmentState {

    public static String S_DRAFT = "DRAFT";
    public static String S_PUBLISHED = "PUBLISHED";
    public static String S_OPEN = "OPEN";
    public static String S_CLOSED = "CLOSED";
    public static String S_FINALIZED = "FINALIZED";

    public static AssignmentState DRAFT = new AssignmentState("DRAFT");
    public static AssignmentState PUBLISHED = new AssignmentState("PUBLISHED");
    public static AssignmentState OPEN = new AssignmentState("OPEN");
    public static AssignmentState CLOSED = new AssignmentState("CLOSED");
    public static AssignmentState FINALIZED = new AssignmentState("FINALIZED");

    @Id
    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentState that = (AssignmentState) o;
        return state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}
