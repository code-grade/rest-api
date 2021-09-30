package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Embeddable
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Validated
public class FinalGrade {

    @NotNull @Min(0) @Max(100)
    private Double finalGrade;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Temporal(TemporalType.TIMESTAMP)
    private Date gradedTime;
}
