package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/auth/user")
    public Map<String, Object> registerUser(@Valid @RequestBody UserAccount userAccount) {
        userAccount.setIsEnabled(false);
        userAccount.setIsVerified(true);
        return RBuilder.success().setData(
                "userId", userService.addUser(userAccount).getUserId()
        ).compact();
    }

    @GetMapping(path = {"/auth/user"})
    public Map<String, Object> getCurrentUser(Principal principal) {
        return RBuilder.success()
                .setData(
                        userService.getUserDetails(UUID.fromString(principal.getName()))
                                .orElseThrow(() -> ApiException.withRBuilder(
                                        RBuilder.notFound().setMsg("User not found")
                                ))
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/auth/user/all")
    public Map<String, Object> getAllUsers() {

        return RBuilder.success()
                .setData(
                        userService.getAllUsers().stream()
                                .map(UserAccount.UserWithoutPass::fromUserAccount)
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = {"/auth/user/{userId}"})
    public Map<String, Object> getUserDetails(@PathVariable("userId") String userId) {
        return RBuilder.success()
                .setData(
                        userService.getUserDetails(UUID.fromString(userId))
                                .orElseThrow(() -> ApiException.withRBuilder(
                                        RBuilder.notFound().setMsg("User not found")
                                ))
                )
                .compact();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(path = {"/auth/user/{userId}/enable/{enabled}"})
    public Map<String, Object> changeUserState(
            @PathVariable("userId") String userId,
            @PathVariable("enabled") Boolean enabled) {

        var user = userService.getUserDetails(UUID.fromString(userId))
                .orElseThrow(() -> ApiException.withRBuilder(RBuilder.notFound("userId is not exists")));

        if (user.getIsEnabled() == enabled) {
            throw ApiException.withRBuilder(RBuilder
                    .badRequest("user account is already " + (enabled ? "enabled" : "disabled"))
                    .setData(UserAccount.UserWithoutPass.fromUserAccount(user))
            );
        }

        user.setIsEnabled(enabled);
        return RBuilder.success()
                .setData(UserAccount.UserWithoutPass.fromUserAccount(userService.updateUser(user)))
                .compact();
    }
}
