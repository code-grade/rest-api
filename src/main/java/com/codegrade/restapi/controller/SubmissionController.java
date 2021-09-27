package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.SourceCode;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.service.SubmissionService;
import com.codegrade.restapi.utils.AuthContext;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @Secured(UserRole.ROLE_STUDENT)
    @PostMapping(path = "/submission/{assignmentId}/{questionId}")
    public ResponseEntity<?> makeSubmission(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @PathVariable("questionId") @VUUID String questionId,
            @RequestBody @Valid SourceCode sourceCode) {

        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(submissionService.makeSubmission(
                        UUID.fromString(assignmentId),
                        UUID.fromString(questionId),
                        context.getUser(),
                        sourceCode
                ))
                .compactResponse();
    }

    @Secured({UserRole.ROLE_STUDENT})
    @GetMapping(path = "/submission/{assignmentId}/{questionId}")
    public ResponseEntity<?> getQuestionSubmissionByStudent(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @PathVariable("questionId") @VUUID String questionId) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(submissionService.getAllSubmissionsByStudent(
                        UUID.fromString(assignmentId),
                        UUID.fromString(questionId),
                        context.getUser()
                ))
                .compactResponse();
    }


    @Secured({UserRole.ROLE_INSTRUCTOR})
    @GetMapping(path = "/submission/{assignmentId}/{questionId}/{studentId}")
    public ResponseEntity<?> getQuestionSubmissionByStudent(
            @PathVariable("assignmentId") @VUUID String assignmentId,
            @PathVariable("questionId") @VUUID String questionId,
            @PathVariable("studentId") @VUUID String studentId) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(submissionService.getAllSubmissionsByStudent(
                        UUID.fromString(assignmentId),
                        UUID.fromString(questionId),
                        UUID.fromString(studentId)
                ))
                .compactResponse();
    }
}
