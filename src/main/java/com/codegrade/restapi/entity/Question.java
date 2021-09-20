package com.codegrade.restapi.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private String subject;
    private String description;
    private String difficulty;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<TestCase> testCases;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<TestCase> evaluationCases;
}
