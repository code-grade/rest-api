package com.codegrade.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greet {

    @GetMapping("/")
    public String greet() {
        return "Code Grade Backend";
    }

    @GetMapping("/api")
    public String greetApi() {
        return "Code Grade Backend API";
    }
}
