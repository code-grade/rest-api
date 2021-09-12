package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.RBuilder;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/auth/user")
    public Map<String, Object> registerUser(@RequestBody UserAccount userAccount) {
        return RBuilder.success().setData(
                "userId", userService.addUser(userAccount).getUserId()
        ).compact();
    }

    @GetMapping(path = "/auth/user")
    public Principal getUsers(Principal principal) {
        return principal;
    }
}
