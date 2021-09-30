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

        public static ParticipationId fromIds(UUID userId, UUID assignmentId) {
            return new ParticipationId(userId, assignmentId);
        }
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

    @Embedded
    private FinalGrade finalGrade;

    @Data
    @AllArgsConstructor @NoArgsConstructor
    public static class LightWeight {
        private User.UserWithoutPass user;
        private Date enrollmentDate;
        private FinalGrade finalGrade;

        public static LightWeight fromParticipation(Participation p) {
            return new LightWeight(
              User.UserWithoutPass.fromUser(p.getUser()),
              p.getEnrollmentDate(),
              p.getFinalGrade()
            );
        }
    }
}
