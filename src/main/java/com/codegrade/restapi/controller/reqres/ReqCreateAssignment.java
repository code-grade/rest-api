package com.codegrade.restapi.controller.reqres;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.AssignmentSchedule;
import com.codegrade.restapi.entity.AssignmentState;
import com.codegrade.restapi.entity.AssignmentType;
import com.codegrade.restapi.utils.validator.VAssignmentState;
import com.codegrade.restapi.utils.validator.VAssignmentType;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ReqCreateAssignment {
    private @NotBlank String title;
    private @NotBlank String description;
    private @VAssignmentType @NotBlank String type;
    private @VAssignmentState String state = AssignmentState.S_DRAFT;
    private List<@VUUID String> questions = new ArrayList<>();
    private @NotNull Boolean scheduled;
    private @Future Date openTime = null;
    private @Future Date closeTime = null;

    public Assignment getAssignment() {
        return new Assignment(null, title, description,
                new HashSet<>(), new HashSet<>(), null,
                new AssignmentState(state), new AssignmentType(type),
                new AssignmentSchedule(scheduled, 0, openTime, closeTime)
        );
    }

    public List<UUID> getQuestions() {
        return questions.stream().map(UUID::fromString)
                .collect(Collectors.toList());
    }
}
