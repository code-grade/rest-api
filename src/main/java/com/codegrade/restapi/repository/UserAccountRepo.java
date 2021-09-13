package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByUsername(String username);

}
