package com.codegrade.restapi.controller;

import com.codegrade.restapi.runtime.ReqRunCode;
import com.codegrade.restapi.service.RuntimeService;
import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RuntimeController {

    private RuntimeService runtimeService;

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
}
