package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.Email;
import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.EmailRepo;
import com.codegrade.restapi.repository.UserRepo;
import com.codegrade.restapi.utils.BPEncoder;
import com.codegrade.restapi.utils.JwtUtils;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final EmailRepo emailRepo;
    private final BPEncoder bpEncoder;
    private final MailService mailService;
    private final JwtUtils jwtUtils;

    /**
     * Retrieve list of users
     * @return - List of users
     */
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    /**
     * Get single user by user-id
     * @param userId - UUID
     * @return User
     */
    public Optional<User> getUserDetails(UUID userId) {
        return userRepo.findById(userId);
    }

    /**
     * Get single user by username
     * @param username - String
     * @return - User
     */
    public Optional<User> getUserDetails(String username) {
        return userRepo.findByUsername(username);
    }

    /**
     * Add new user to system
     * @param newUser - User
     * @return - Complete user object with user-id
     */
    @Transactional
    public User addUser(User newUser) {
        // Check for duplicate usernames
        var user = userRepo.findByUsername(newUser.getUsername());
        if (user.isPresent()) {
            throw new ApiException(RBuilder.badRequest()
                    .setMsg("username is already in use")
            );
        }

        // check for duplicate email addresses
        var newEmail = newUser.getEmail();
        emailRepo.findById(newEmail.getEmail()).ifPresent((var email) -> {
            throw new ApiException(RBuilder.badRequest("email address is already in use"));
        });

        // adding new user and email address
        try {
            // persist email address
            emailRepo.saveAndFlush(newEmail);

            // persist user data
            newUser.setPassword(bpEncoder.encode(newUser.getPassword()));
            newUser = userRepo.save(newUser);
            mailService.verificationEmail(newUser.getUsername(), newUser.getEmail().getEmail());
            return newUser;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new ApiException(RBuilder.error());
        }
    }

    /**
     * Verify Email address
     * @param token - JWT token which contains email verification details
     * @return - Verified email
     */
    public Email verifyEmailAddress(String token) {
        var verificationData = jwtUtils.parseEmailJwt(token);
        var email = emailRepo.findById(verificationData.getEmail())
                .orElseThrow(() -> new ApiException(RBuilder.badRequest("invalid email verification request")));

        email.setVerified(true);
        emailRepo.save(email);
        return email;
    }

    /**
     * Send verification email again
     * @param userId - send verification email
     * @return - Email
     */
    public Email sendVerificationRequest(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(RBuilder.badRequest("incorrect user id")));
        var email = user.getEmail();
        if (email.getVerified()) {
            throw new ApiException(RBuilder.badRequest("associated email address is already verified"));
        }
        mailService.verificationEmail(user.getUsername(), email.getEmail());
        return emailRepo.save(email);
    }

    /**
     * Change user state, either enabled or disabled
     * @param userId - user id
     * @param enabled - Boolean | enable or disabled
     * @return - Changed user data
     */
    public User.UserWithoutPass changeState(UUID userId, Boolean enabled) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(RBuilder.badRequest("incorrect user id")));
        if (user.getIsEnabled() == enabled) {
            throw new ApiException(RBuilder.badRequest(
                    enabled? "already enabled user account": "user account is already disabled"
            ));
        } else {
            user.setIsEnabled(enabled);
            userRepo.save(user);
        }
        return User.UserWithoutPass.fromUser(user);
    }

    /**
     * Update user details such as First name, Last name, email
     * @param userId - UUID of user
     * @param newUser - update details
     * @return - Updated user object
     */
    public User.UserWithoutPass updateUser(UUID userId, User newUser) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(RBuilder.badRequest("incorrect user id")));
        if (newUser.getFirstName() != null) user.setFirstName(newUser.getFirstName());
        if (newUser.getLastName() != null) user.setLastName(newUser.getLastName());
//        if (newUser.getEmail() != null) user.setEmail(newUser.getEmail());
        return User.UserWithoutPass.fromUser(userRepo.save(user));
    }

    /**
     * Find user by username and return user details
     * @param username - username
     * @return - UserDetails
     * @throws UsernameNotFoundException - when user is not exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return AppUser.appUserBuilder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getRole())
                .disabled(!user.getIsEnabled())
                .build();
    }


}
