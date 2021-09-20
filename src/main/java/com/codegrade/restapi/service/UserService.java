package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.EmailRepo;
import com.codegrade.restapi.repository.UserRepo;
import com.codegrade.restapi.utils.BPEncoder;
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

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserDetails(UUID userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUserDetails(String username) {
        return userRepo.findByUsername(username);
    }

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserDetails(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return AppUser.appUserBuilder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getRole())
                .disabled(!user.getIsEnabled())
                .build();
    }

    public User updateUser(User user) {
        return userRepo.save(user);
    }

}
