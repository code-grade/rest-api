package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

//@Entity
//@Table(name = "assignment_questions")
//@IdClass(AssignmentQuestion.AssignmentQuestionId.class)
//@AllArgsConstructor @NoArgsConstructor
//@Getter @Setter
public class AssignmentQuestion {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static public class AssignmentQuestionId implements Serializable {
        private UUID assignmentId;
        private UUID questionId;
    }

    @Id
    private UUID assignmentId;

    @Id
    private UUID questionId;

}
