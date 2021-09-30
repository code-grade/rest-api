package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.SourceCode;
import com.codegrade.restapi.runtime.ReqRunCode;
import com.codegrade.restapi.service.EvaluationService;
import com.codegrade.restapi.service.RuntimeService;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VUUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class RuntimeController {

    private RuntimeService runtimeService;
    private EvaluationService evaluationService;

    @PostMapping("/runtime/run")
    public ResponseEntity<?> runPythonCode(@RequestBody @Valid ReqRunCode reqRunCode) {
       return RBuilder.success()
               .setData(runtimeService.runCode(
                       reqRunCode.getSource(),
                       reqRunCode.getInput(),
                       reqRunCode.getLanguage()
               ))
               .compactResponse();
    }

    @PostMapping("/runtime/test/{questionId}")
    public ResponseEntity<?> runTestCases(
            @PathVariable("questionId") @VUUID String questionId,
            @RequestBody @Valid SourceCode sourceCode) {
        return RBuilder.success()
                .setData(evaluationService.evaluationTest(sourceCode, UUID.fromString(questionId)))
                .compactResponse();
    }
}
