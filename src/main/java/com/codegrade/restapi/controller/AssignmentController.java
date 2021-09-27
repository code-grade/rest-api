package com.codegrade.restapi.controller;

import com.codegrade.restapi.controller.reqres.ReqCreateAssignment;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.service.AssignmentService;
import com.codegrade.restapi.utils.AuthContext;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Getter
@Setter
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @PostMapping(path = "/assignment")
    public ResponseEntity<?> create(
            @RequestBody @Valid ReqCreateAssignment req
    ) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.create(
                        context.getUser(),
                        req.getAssignment(),
                        req.getQuestions()
                ))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/assignment")
    public ResponseEntity<?> getByInstructor() {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.getByInstructor(context.getUser()))
                .compactResponse();
    }

    @GetMapping(path = "/assignment/{assignmentId}")
    public ResponseEntity<?> getById(@PathVariable("assignmentId") @VUUID String assignmentId) {
        return RBuilder.success()
                .setData(assignmentService.getAssignmentById(UUID.fromString(assignmentId)))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/assignment/participate/{assignmentId}")
    public ResponseEntity<?> getParticipantsById(@PathVariable("assignmentId") @VUUID String assignmentId) {
        return RBuilder.success()
                .setData(assignmentService.getParticipantsById(UUID.fromString(assignmentId)))
                .compactResponse();
    }


    @Secured(UserRole.ROLE_STUDENT)
    @PutMapping(path = "/assignment/participate/{assignmentId}")
    public ResponseEntity<?> participate(@PathVariable("assignmentId") @VUUID String assignmentId) {
        var context = AuthContext.fromContextHolder();
        assignmentService.addParticipant(UUID.fromString(assignmentId), context.getUser());
        return RBuilder.success()
                .compactResponse();
    }

    @Secured(UserRole.ROLE_STUDENT)
    @GetMapping(path = "/assignment/participate")
    public ResponseEntity<?> getParticipatedAssignments() {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.getAssignmentByStudent(context.getUser()))
                .compactResponse();
    }

}
