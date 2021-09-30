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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private final EvaluationService evaluationService;

    public Submission.LightWeight makeSubmission(UUID assignmentId, UUID questionId, User user, SourceCode source) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignmentId is invalid")));
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("questionId is invalid")));
        Submission submission = new Submission(assignment, question, user, source);
        submission = submissionRepo.save(submission);
        evaluationService.evaluate(submission);
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


    public List<Submission.WithQuestion> getStudentSubmissionSummary(UUID assignmentId, UUID studentId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid student id")));
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid assignment id")));
        Sort maximumPoints = Sort.by(Sort.Direction.DESC, "result.totalPoints" );
        return assignment.getQuestions().stream()
                .map(q -> submissionRepo.findDistinctFirstByAssignmentAndUser(assignment, student, maximumPoints)
                        .orElse(null))
                .filter(Objects::nonNull)
                .map(Submission.WithQuestion::fromSubmission)
                .collect(Collectors.toList());
    }

    public Submission.LightWeight getSubmissionById(UUID submissionId) {
        return submissionRepo.findById(submissionId)
                .map(Submission.LightWeight::fromSubmission)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid submission id")));
    }
}
