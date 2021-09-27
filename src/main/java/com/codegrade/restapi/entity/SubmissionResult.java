package com.codegrade.restapi.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Embeddable
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SubmissionResult {

    private Boolean evaluated = false;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<TestCaseResult> testCases;

    @Temporal(TemporalType.TIMESTAMP)
    private Date evaluatedTime;

    private Integer totalPoints;

}
