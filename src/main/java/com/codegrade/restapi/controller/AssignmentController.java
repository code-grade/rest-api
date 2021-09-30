package com.codegrade.restapi.controller;

import com.codegrade.restapi.controller.reqres.ReqCreateAssignment;
import com.codegrade.restapi.entity.AssignmentState;
import com.codegrade.restapi.entity.FinalGrade;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.service.AssignmentService;
import com.codegrade.restapi.utils.AuthContext;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VAssignmentState;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Getter
@Setter
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @PostMapping(path = "/assignment/create")
    public ResponseEntity<?> CREATE_Assignment(@RequestBody @Valid ReqCreateAssignment req) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.createAssignment(
                        context.getUser(),
                        req.getAssignment(),
                        req.getQuestions()
                ))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @PutMapping(path = "/assignment/{assignmentId}")
    public ResponseEntity<?> EDIT_Assignment(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @RequestBody @Valid ReqCreateAssignment req) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.updateAssignment(
                        UUID.fromString(assignmentId),
                        req.getAssignment(),
                        req.getQuestions()
                )).compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @PutMapping(path = "/assignment/{assignmentId}/state/{state}")
    public ResponseEntity<?> CHANGE_AssignmentState(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @PathVariable("state") @VAssignmentState String state) {
        return RBuilder.success()
                .setData(
                        assignmentService.changeAssignmentState(UUID.fromString(assignmentId),
                                new AssignmentState(state.toUpperCase()))
                ).compactResponse();
    }

    @Secured(UserRole.ROLE_STUDENT)
    @PostMapping(path = "/assignment/participate/{assignmentId}")
    public ResponseEntity<?> ENROLL_Participation(@PathVariable("assignmentId") @VUUID String assignmentId) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.enrollToAssignment(UUID.fromString(assignmentId), context.getUser()))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_STUDENT)
    @DeleteMapping(path = "/assignment/participate/{assignmentId}")
    public ResponseEntity<?> UNENROLL_Participation(@PathVariable("assignmentId") @VUUID String assignmentId) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.unEnrollFromAssignment(UUID.fromString(assignmentId), context.getUser()))
                .compactResponse();
    }

    @GetMapping(path = "/assignment/{assignmentId}")
    public ResponseEntity<?> GET_AssignmentById(@PathVariable("assignmentId") @VUUID String assignmentId) {
        return RBuilder.success()
                .setData(assignmentService.getAssignmentById(UUID.fromString(assignmentId)))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/assignment/instructor")
    public ResponseEntity<?> GET_AssignmentsByInstructor(
            @RequestParam(value = "state", required = false)
            @VAssignmentState Optional<String> state) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(state
                        .map(s -> assignmentService.getByInstructor(context.getUser(),
                                new AssignmentState(s)))
                        .orElse(assignmentService.getByInstructor(context.getUser())))
                .compactResponse();
    }


    @Secured({UserRole.ROLE_STUDENT, UserRole.ROLE_INSTRUCTOR})
    @GetMapping(path = "/assignment/public")
    public ResponseEntity<?> GET_PublicAssignments(
            @RequestParam(value = "state", required = false)
            @VAssignmentState Optional<String> state) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(state
                        .map(s -> assignmentService.getPublicAssignments(new AssignmentState(s), context.getUser()))
                        .orElse(assignmentService.getPublicAssignments(context.getUser())))
                .compactResponse();
    }


    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/assignment/participate/{assignmentId}")
    public ResponseEntity<?> GET_ParticipantsByAssignmentId(
            @PathVariable("assignmentId") @VUUID String assignmentId) {
        return RBuilder.success()
                .setData(assignmentService.getParticipantsById(UUID.fromString(assignmentId)))
                .compactResponse();
    }


    @Secured(UserRole.ROLE_STUDENT)
    @GetMapping(path = "/assignment/participate")
    public ResponseEntity<?> GET_AssignmentsByParticipant(
            @RequestParam(value = "state", required = false) @VAssignmentState Optional<String> state
    ) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(state.
                        map(s -> assignmentService.getAssignmentByStudent(context.getUser(), new AssignmentState(s)))
                        .orElse(assignmentService.getAssignmentByStudent(context.getUser()))
                )
                .compactResponse();
    }

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @PostMapping(path = "/assignment/{assignmentId}/grade/{studentId}")
    public ResponseEntity<?> GET_AssignmentsByParticipant(
            @PathVariable("studentId") @VUUID String studentId,
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @RequestBody @Valid FinalGrade finalGrade
    ) {
        return RBuilder.success()
                .setData(assignmentService.submitFinalGrade(
                        UUID.fromString(assignmentId),
                        UUID.fromString(studentId),
                        finalGrade)
                ).compactResponse();
    }


    @Secured({ UserRole.ROLE_STUDENT})
    @GetMapping(path = "/assignment/{assignmentId}/grade")
    public ResponseEntity<?> GET_AssignmentsByParticipant(
            @PathVariable("assignmentId") @VUUID String assignmentId
    ) {
        AuthContext context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(assignmentService.getFinalGrade(
                        UUID.fromString(assignmentId),
                        context.getUser()
                )).compactResponse();
    }


    @Secured({ UserRole.ROLE_INSTRUCTOR})
    @GetMapping(path = "/assignment/{assignmentId}/student/{studentId}/grade")
    public ResponseEntity<?> GET_AssignmentsByParticipant(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @PathVariable("studentId") @VUUID String studentId
    ) {
        return RBuilder.success()
                .setData(assignmentService.getFinalGrade(
                        UUID.fromString(assignmentId),
                        UUID.fromString(studentId)
                )).compactResponse();
    }
}
