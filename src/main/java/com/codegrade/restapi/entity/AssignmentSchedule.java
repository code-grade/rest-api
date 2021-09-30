package com.codegrade.restapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AssignmentSchedule {

    private Boolean isScheduled = false;

    @JsonIgnore
    private Integer scheduleJobId = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date openTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closeTime;

    @JsonIgnore
    public Boolean isValid() {
        return !isScheduled || (openTime != null && closeTime != null);
    }
}
