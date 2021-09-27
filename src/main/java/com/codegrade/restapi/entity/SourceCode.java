package com.codegrade.restapi.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SourceCode {

    private String source;
    private String language;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SourceCode that = (SourceCode) o;
        return Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}
