package com.codegrade.restapi.controller.reqres;


import com.codegrade.restapi.entity.Question;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ResAssignmentWithParticipants implements Serializable {
    private UUID assignmentId;
    private String title;
    private String description;
    private UUID instructor;
    private List<Question.NoTestCase> questions;
    private String state;
    private String type;
    private Date openTime;
    private Date closeTime;
    private Set<UUID> participants;
}
