package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.models.EmailVerificationReq;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.AuthContext;
import com.codegrade.restapi.utils.validator.OptionalUUID;
import com.codegrade.restapi.utils.RBuilder;
import com.codegrade.restapi.utils.validator.VUUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RestController
@Validated
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/auth/user")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        return RBuilder.success().setData(
                User.UserWithoutPass.fromUser(userService.addUser(user))
        ).compactResponse();
    }

    /**
     * Get user details
     */
    @GetMapping(path = {"/auth/user", "/auth/user/{userId}"})
    public ResponseEntity<?> getUserDetails(
            @OptionalUUID(message = "invalid user id")
            @PathVariable(value = "userId", required = false)
                    Optional<String> userId) {

        var context = AuthContext.fromContextHolder();

        // Try to access user data and user is not an admin
        if ( userId.isPresent() && !context.isAdmin() && !context.matchesUserId(userId)) {
            throw new ApiException(RBuilder.unauthorized());
        }

        var uid = userId.filter(s -> context.isAdmin()).map(UUID::fromString).orElse(context.getUserId());

        return RBuilder.success()
                .setData(
                        User.UserWithoutPass.fromUser(
                                userService.getUserDetails(uid)
                                        .orElseThrow(() -> ApiException.withRBuilder(
                                                RBuilder.notFound().setMsg("User not found")
                                        )))
                )
                .compactResponse();
    }

    @Secured(UserRole.ROLE_ADMIN)
    @GetMapping(path = "/auth/user/all")
    public Map<String, Object> getAllUsers() {
        return RBuilder.success()
                .setData(userService.getAllUsers().stream().map(User.UserWithoutPass::fromUser))
                .compact();
    }

    @PutMapping(path = "/auth/user")
    public ResponseEntity<?> updateUserDetails(@RequestBody User user) {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success()
                .setData(userService.updateUser(context.getUserId(), user))
                .compactResponse();
    }

    @Secured(UserRole.ROLE_ADMIN)
    @PutMapping(path = "/auth/user/{userId}/enable/{enabled}")
    public ResponseEntity<?> changeUserState(
            @PathVariable("userId") @VUUID(message = "invalid user id") String userId,
            @PathVariable("enabled") Boolean enabled
    ) {
        return RBuilder.success()
                .setData(userService.changeState(UUID.fromString(userId), enabled))
                .compactResponse();
    }


    @PostMapping(path = "/auth/verify")
    public ResponseEntity<?> verifyEmailAddress(@RequestBody EmailVerificationReq req) {
        return RBuilder.success()
                .setData(userService.verifyEmailAddress(req.getToken()))
                .compactResponse();
    }

    @PostMapping(path = "/auth/verify-req")
    public ResponseEntity<?> verifyEmailTokenRequest() {
        var context = AuthContext.fromContextHolder();
        return RBuilder.success("verification email has sent")
                .setData(userService.sendVerificationRequest(context.getUserId()))
                .compactResponse();
    }

}
