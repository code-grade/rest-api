package com.codegrade.restapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "participation")
@IdClass(Participation.ParticipationId.class)
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class Participation {

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    static public class ParticipationId implements Serializable {
        private UUID user;
        private UUID assignment;
    }

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date enrollmentDate = new Date();
}
