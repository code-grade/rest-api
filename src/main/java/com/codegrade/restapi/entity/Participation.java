package com.codegrade.restapi.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "participation")
@IdClass(Participation.ParticipationId.class)
public class Participation {

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    static public class ParticipationId implements Serializable {
        private User user;
        private Assignment assignment;
    }


    @Id
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date enrollmentDate = new Date();

}
