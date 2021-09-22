package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "assignment_questions")
@IdClass(AssignmentQuestion.AssignmentQuestionId.class)
public class AssignmentQuestion {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static public class AssignmentQuestionId implements Serializable {
        private Assignment assignment;
        private Question question;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "assignmentId")
    private Assignment assignment;

    @Id
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

}
