package com.codegrade.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("/")
    public String about() {
        return "Code Grade Backend Rest API";
    }
}
