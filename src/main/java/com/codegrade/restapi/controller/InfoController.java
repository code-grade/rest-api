package com.codegrade.restapi.controller;

import com.codegrade.restapi.config.MailServer;
import com.codegrade.restapi.service.MailService;
import com.codegrade.restapi.utils.RBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final MailService mailService;

    @GetMapping("/")
    public String about() {
        return "Code Grade Backend Rest API";
    }

}
