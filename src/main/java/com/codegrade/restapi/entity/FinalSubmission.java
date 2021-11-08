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
public class FinalSubmission {

    @Id
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

    public static FinalSubmission fromSubmission(Submission s) {
        return new FinalSubmission(
                s.getSubmissionId(),
                s.getAssignment(),
                s.getQuestion(),
                s.getUser(),
                s.getSourceCode(),
                s.getResult(),
                s.getSubmittedTime()
        );
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class WithUser {
        private UUID submissionId;
        private UUID assignmentId;
        private UUID questionId;
        private User.UserWithoutPass user;
        private SourceCode sourceCode;
        private Date submittedTime;

        public static WithUser fromFinalSubmission(FinalSubmission fs) {
            return WithUser.builder()
                    .submissionId(fs.getSubmissionId())
                    .assignmentId(fs.getAssignment().getAssignmentId())
                    .questionId(fs.getQuestion().getQuestionId())
                    .user(User.UserWithoutPass.fromUser(fs.getUser()))
                    .sourceCode(fs.getSourceCode())
                    .submittedTime(fs.getSubmittedTime())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class WithQuestion {
        private UUID submissionId;
        private UUID assignmentId;
        private Question.LightWeight question;
        private SourceCode sourceCode;
        private SubmissionResult result;
        private Date submittedTime;

        public static FinalSubmission.WithQuestion fromSubmission(FinalSubmission s) {
            return FinalSubmission.WithQuestion.builder()
                    .submissionId(s.submissionId)
                    .assignmentId(s.getAssignment().getAssignmentId())
                    .question(Question.LightWeight.fromQuestion(s.getQuestion()))
                    .sourceCode(s.getSourceCode())
                    .result(s.getResult())
                    .submittedTime(s.getSubmittedTime()).build();
        }
    }
}
