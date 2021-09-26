package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
}
