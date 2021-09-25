package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Getter @Setter
public class AssignmentController {

    @PostMapping(path = "/assignment/")
    public ResponseEntity<?> create(@RequestBody Assignment assignment) {
        return RBuilder.success()
                .setMsg("Assignment created")
                .compactResponse();
    }

}
