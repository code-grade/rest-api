package com.codegrade.restapi.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID submissionId;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    @Column(nullable = false)
    private SourceCode sourceCode;

    @Embedded
    @Column(nullable = false)
    private SubmissionResult result = new SubmissionResult();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedTime = new Date();

    public Submission(Assignment assignment, Question question, User user, SourceCode sourceCode) {
        this.assignment = assignment;
        this.question = question;
        this.user = user;
        this.sourceCode = sourceCode;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class LightWeight {
        private UUID submissionId;
        private UUID assignmentId;
        private UUID questionId;
        private UUID userId;
        private SourceCode sourceCode;
        private SubmissionResult result;
        private Date submittedTime;

        public static LightWeight fromSubmission(Submission s) {
           return LightWeight.builder()
                   .submissionId(s.submissionId)
                   .assignmentId(s.getAssignment().getAssignmentId())
                   .questionId(s.getQuestion().getQuestionId())
                   .userId(s.getUser().getUserId())
                   .sourceCode(s.getSourceCode())
                   .result(s.getResult())
                   .submittedTime(s.getSubmittedTime()).build();
        }

    }
}
