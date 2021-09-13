package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.*;
import java.security.Principal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/auth/user")
    public Map<String, Object> registerUser(@Valid @RequestBody UserAccount userAccount) {
        return RBuilder.success().setData(
                "userId", userService.addUser(userAccount).getUserId()
        ).compact();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/auth/user")
    public Map<String, Object> getAllUsers()  {

        return RBuilder.success()
                .setData(
                        userService.getAllUsers().stream()
                        .map(UserAccount.UserWithoutPass::fromUserAccount)
                )
                .compact();
    }

}
