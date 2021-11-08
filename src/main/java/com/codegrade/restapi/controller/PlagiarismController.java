package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.service.PlagiarismService;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Getter
@Setter
public class PlagiarismController {

    private final PlagiarismService plagiarismService;

    @Secured(UserRole.ROLE_INSTRUCTOR)
    @GetMapping(path = "/plagiarism/report/{assignmentId}/{questionId}")
    public Map<String, Object> getPlagiarismReport(
            @PathVariable @VUUID String assignmentId,
            @PathVariable @VUUID String questionId
    ) {
        return RBuilder.success()
                .setData(plagiarismService.generatePlagiarismReport(
                        UUID.fromString(assignmentId),
                        UUID.fromString(questionId)
                ))
                .compact();
    }
}
