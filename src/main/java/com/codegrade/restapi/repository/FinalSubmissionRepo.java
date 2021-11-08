package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.FinalSubmission;
import com.codegrade.restapi.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinalSubmissionRepo extends JpaRepository<FinalSubmission, UUID> {
    void deleteAllByAssignment(Assignment assignment);

    List<FinalSubmission> findAllByAssignmentAndQuestion(Assignment assignment, Question question);
}