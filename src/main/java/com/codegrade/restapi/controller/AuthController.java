package com.codegrade.restapi.controller;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.entity.UserRole;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.service.UserService;
import com.codegrade.restapi.utils.IsUUID;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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
    @Secured({UserRole.ROLE_ADMIN, UserRole.ROLE_STUDENT, UserRole.ROLE_INSTRUCTOR})
    @GetMapping(path = {"/auth/user", "/auth/user/{userId}"})
    public ResponseEntity<?> getUserInfo(
            @PathVariable(value = "userId", required = false) Optional<String> reqUserId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId;
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_ADMIN))) {
            userId = UUID.fromString(reqUserId.orElse(authentication.getName()));
        } else {
            if (reqUserId.isPresent() && !reqUserId.get().equals(authentication.getName())) {
                throw new ApiException(RBuilder.unauthorized());
            }
            userId = UUID.fromString(authentication.getName());
        }

        return RBuilder.success()
                .setData(
                        User.UserWithoutPass.fromUser(
                                userService.getUserDetails((UUID) userId)
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
                .setData(
                        userService.getAllUsers().stream()
                                .map(User.UserWithoutPass::fromUser)
                )
                .compact();
    }


    @Secured(UserRole.ROLE_ADMIN)
    @PutMapping(path = "/auth/user/{userId}/enable/{enabled}")
    public Map<String, Object> changeUserState(
            @PathVariable("userId") @IsUUID(message = "invalid user id") String userId,
            @PathVariable("enabled") Boolean enabled
    ) {

        var user = userService.getUserDetails(UUID.fromString(userId))
                .orElseThrow(() -> ApiException.withRBuilder(RBuilder.notFound("userId is not exists")));

        if (user.getIsEnabled() == enabled) {
            throw ApiException.withRBuilder(RBuilder
                    .badRequest("user account is already " + (enabled ? "enabled" : "disabled"))
                    .setData(User.UserWithoutPass.fromUser(user))
            );
        }

        user.setIsEnabled(enabled);
        return RBuilder.success()
                .setData(User.UserWithoutPass.fromUser(userService.updateUser(user)))
                .compact();
    }

    @Secured(UserRole.ROLE_ADMIN)
    @PutMapping(path = "/auth/user/{userId}")
    public Map<String, Object> updateUserDetails(
            @PathVariable("userId") @IsUUID(message = "invalid user id") String userId,
            @RequestBody User user,
            Principal principal
    ) {
        return null;
    }
}
