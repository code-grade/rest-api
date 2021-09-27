package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.repository.SubmissionRepo;
import com.codegrade.restapi.repository.UserRepo;
import com.codegrade.restapi.utils.RBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class SubmissionService {

    private final SubmissionRepo submissionRepo;
    private final QuestionRepo questionRepo;
    private final AssignmentRepo assignmentRepo;
    private final UserRepo userRepo;

    public Submission.LightWeight makeSubmission(UUID assignmentId, UUID questionId, User user, SourceCode source) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignmentId is invalid")));
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("questionId is invalid")));
        Submission submission = new Submission(assignment, question, user, source);
        return Submission.LightWeight.fromSubmission(submissionRepo.save(submission));
    }

    public Set<Submission.LightWeight> getAllSubmissionsByStudent(UUID assignmentId, UUID questionId, User user) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignmentId is incorrect")));
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("questionId is incorrect")));
        return submissionRepo
                .findSubmissionByAssignmentAndQuestionAndUser(assignment, question, user)
                .stream().map(Submission.LightWeight::fromSubmission).collect(Collectors.toSet());
    }

    public Set<Submission.LightWeight> getAllSubmissionsByStudent(UUID assignmentId, UUID questionId, UUID userId) {
        User student = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("userId is incorrect")));
        return getAllSubmissionsByStudent(assignmentId, questionId, student);

    }


    // 3 evaluate student submission


}
