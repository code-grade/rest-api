package com.codegrade.restapi.controller.reqres;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.AssignmentSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class ResItemPublicAssignment {

    private UUID assignmentId;
    private String title;
    private String description;
    private UUID instructorId;
    private String state;
    private String type;
    private AssignmentSchedule schedule;
    private Boolean enrolled;

   public static ResItemPublicAssignment fromAssignment(Assignment as, Boolean enrolled) {
       return new ResItemPublicAssignment(
               as.getAssignmentId(),
               as.getTitle(),
               as.getDescription(),
               as.getInstructor().getUserId(),
               as.getState().getState(),
               as.getType().getType(),
               as.getSchedule(),
               enrolled
       );
   }

}
