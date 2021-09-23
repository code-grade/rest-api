package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID assignmentId;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment")
    private List<Participation> participants;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment")
    private List<AssignmentQuestion> questions;

    @ManyToOne
    @JoinColumn(name = "state")
    private AssignmentState state;

    @ManyToOne
    @JoinColumn(name = "type")
    private AssignmentType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date openTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closeTime;

}
