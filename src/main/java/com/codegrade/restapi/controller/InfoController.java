package com.codegrade.restapi.controller;

import com.codegrade.restapi.service.JobService;
import com.codegrade.restapi.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final MailService mailService;
    private final JobService jobService;

    @GetMapping("/")
    public String about() {
        return "Code Grade Backend Rest API";
    }

}
