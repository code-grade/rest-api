package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.UserAccount;
import com.codegrade.restapi.repository.UserAccountRepository;
import com.codegrade.restapi.utils.BPEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserAccountRepository userAccRepo;
    private final BPEncoder bpEncoder;

    public UserAccount addUser(UserAccount userAccount) {
        userAccount.setPassword(bpEncoder.encode(userAccount.getPassword()));
        return userAccRepo.save(userAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserAccount> userAccountList = userAccRepo.findByUsername(username);
        if (userAccountList.isEmpty()) {
            log.info("User not found");
            throw new UsernameNotFoundException("User doesn't exists");
        }

        var user = userAccountList.get(0);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }
}
