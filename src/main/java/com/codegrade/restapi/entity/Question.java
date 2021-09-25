package com.codegrade.restapi.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Question {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID questionId;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    private String title;
    private String description;
    private String difficulty;
    private Integer totalPoints;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<TestCase> testCases;

    @Data
    @AllArgsConstructor
    static public class JustInstructorId {
        private UUID questionId;
        private UUID instructorId;
        private String title;
        private String description;
        private String difficulty;
        private Integer totalPoints;
        private List<TestCase> testCases;

        public static JustInstructorId fromQuestion(Question q) {
           return new JustInstructorId(
                   q.getQuestionId(),
                   q.getInstructor().getUserId(),
                   q.getTitle(),
                   q.getDescription(),
                   q.getDifficulty(),
                   q.getTotalPoints(),
                   q.getTestCases()
           );
        }
    }

    @Data
    @AllArgsConstructor
    static public class NoTestCase {
        private UUID questionId;
        private UUID instructorId;
        private String title;
        private String description;
        private String difficulty;
        private Integer totalPoints;

        public static NoTestCase fromQuestion(Question q) {
            return new NoTestCase(
                    q.getQuestionId(),
                    q.getInstructor().getUserId(),
                    q.getTitle(),
                    q.getDescription(),
                    q.getDifficulty(),
                    q.getTotalPoints()
            );
        }
    }

}
