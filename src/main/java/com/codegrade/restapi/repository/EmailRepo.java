package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepo extends JpaRepository<Email, String> {
}
