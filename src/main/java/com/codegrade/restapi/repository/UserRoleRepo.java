package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole, String> {
}
