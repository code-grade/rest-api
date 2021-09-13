package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.UserAccountRepo;
import com.codegrade.restapi.utils.BPEncoder;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserAccountRepo userAccRepo;
    private final BPEncoder bpEncoder;

    public List<UserAccount> getAllUsers() {
        return userAccRepo.findAll();
    };

    public Optional<UserAccount> getUserByUsername(String username) {
        return userAccRepo.findByUsername(username);
    }

    public UserAccount addUser(UserAccount newUser) {
        var user = userAccRepo.findByUsername(newUser.getUsername());
        if (user.isPresent()) {
            throw new ApiException(RBuilder.badRequest()
                    .setMsg("username is already in use")
            );
        }

        try {
            newUser.setPassword(bpEncoder.encode(newUser.getPassword()));
            return userAccRepo.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(RBuilder.badRequest()
                    .setMsg("email address is already in use")
            );
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount user = this.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));

        return AppUser.appUserBuilder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.getIsEnabled())
                .build();
    }
}
