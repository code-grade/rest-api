package com.codegrade.restapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "assignment_questions",
            joinColumns = @JoinColumn(name = "assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions;

    @OneToMany(mappedBy = "assignment")
    private Set<Participation> participants;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne
    @JoinColumn(name = "state")
    private AssignmentState state = AssignmentState.DRAFT;

    @ManyToOne
    @JoinColumn(name = "type")
    private AssignmentType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date openTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closeTime;

    public Assignment(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Data
    @AllArgsConstructor
    public static class LightWeight {
        private UUID assignmentId;
        private String title;
        private String description;
        private UUID instructor;
        private List<UUID> questions;
        private String state;
        private String type;
        private Date openTime;
        private Date closeTime;

        public static LightWeight fromAssignment(Assignment as) {
           return new LightWeight(
                   as.getAssignmentId(),
                   as.getTitle(),
                   as.getDescription(),
                   as.getInstructor().getUserId(),
                   as.getQuestions().stream().map(Question::getQuestionId).collect(Collectors.toList()),
                   as.getState().getState(),
                   as.getType().getType(),
                   as.getOpenTime(),
                   as.getCloseTime()
           );
        }
    }

    @Data
    @AllArgsConstructor
    public static class WithQuestions {
        private UUID assignmentId;
        private String title;
        private String description;
        private UUID instructor;
        private List<Question.NoTestCase> questions;
        private String state;
        private String type;
        private Date openTime;
        private Date closeTime;

        public static WithQuestions fromAssignment(Assignment as) {
            return new WithQuestions(
                    as.getAssignmentId(),
                    as.getTitle(),
                    as.getDescription(),
                    as.getInstructor().getUserId(),
                    as.getQuestions().stream().map(Question.NoTestCase::fromQuestion)
                            .collect(Collectors.toList()),
                    as.getState().getState(),
                    as.getType().getType(),
                    as.getOpenTime(),
                    as.getCloseTime()
            );
        }
    }
}
