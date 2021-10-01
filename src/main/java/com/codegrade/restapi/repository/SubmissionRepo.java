package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.entity.Submission;
import com.codegrade.restapi.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
    Set<Submission> findSubmissionByAssignmentAndQuestionAndUser(Assignment assignment, Question question, User user);

    Optional<Submission> findDistinctFirstByAssignmentAndUserAndQuestion(
            Assignment assignment,
            User user,
            Question question,
            Sort sort
    );
}
