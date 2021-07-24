package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountRepository userAccountRepository;

    @PostMapping
    public UserAccount registerUser(@RequestBody UserAccount userAccount) {
        userAccountRepository.save(userAccount);
        return userAccount;
    }

    @GetMapping
    public List<UserAccount> getUsers() {
        return userAccountRepository.findAll();
    }
}
