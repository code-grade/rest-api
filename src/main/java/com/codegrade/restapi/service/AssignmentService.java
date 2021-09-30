package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.ParticipationRepo;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.utils.RBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AssignmentService {

    private final AssignmentRepo assignmentRepo;
    private final QuestionRepo questionRepo;
    private final ParticipationRepo participationRepo;
    private final ObjectMapper objectMapper;

    /**
     * Create new assignment
     *
     * @param instructor - Instructor
     * @param assignment - Assignment Details
     * @return Assignment with complete details
     */
    public Assignment.LightWeight create(User instructor, Assignment assignment, List<UUID> questions) {
        assignment.setInstructor(instructor);
        var ques = questions.stream().map(id ->
                questionRepo.findById(id).orElseThrow(() ->
                        new ApiException(RBuilder.badRequest("invalid question id " + id))
                )
        ).collect(Collectors.toSet());
        assignment.setQuestions(ques);
        var created = assignmentRepo.save(assignment);
        return Assignment.LightWeight.fromAssignment(created);
    }

    /**
     * Get list of assignments by instructor
     *
     * @param instructor - User
     * @return - List of assignments
     */
    public List<Assignment.LightWeight> getByInstructor(User instructor) {
        return assignmentRepo.findAssignmentByInstructor(instructor).stream()
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toList());
    }

    /**
     * Get list of assignments by instructor
     *
     * @param instructor - User
     * @return - List of assignments
     */
    public List<Assignment.LightWeight> getByInstructor(User instructor, String assignmentState) {
        return assignmentRepo
                .findAssignmentByInstructorAndState(instructor, new AssignmentState(assignmentState)).stream()
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toList());
    }

    /**
     * Get assignment by id
     * @param assignmentId - UUID
     * @return assignment details
     */
    public Assignment.WithQuestions getAssignmentById(UUID assignmentId) {
        return assignmentRepo.findById(assignmentId)
                .map(Assignment.WithQuestions::fromAssignment)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
    }


    /**
     * Participate in an assignment
     * @param assignmentId - UUID
     * @param student User
     */
    public void addParticipant(UUID assignmentId, User student) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        Participation part = Participation.builder()
                .assignment(assignment)
                .user(student)
                .enrollmentDate(new Date())
                .build();
        participationRepo.save(part);
    }

    /**
     * Get participated assignments by user
     * @param student - User
     * @return List of assignments
     */
    public Set<Assignment.LightWeight> getAssignmentByStudent(User student) {
      return participationRepo.findParticipationByUser(student).stream()
              .map(Participation::getAssignment)
              .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toSet());
    };

    /**
     * Get list of participants by assignmentId
     * @param assignmentId - UUID
     * @return list of participant users
     */
    public List<User.UserWithoutPass> getParticipantsById(UUID assignmentId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        return participationRepo.findParticipationByAssignment(assignment).stream()
                .map(Participation::getUser)
                .map(User.UserWithoutPass::fromUser).collect(Collectors.toList());
    }

    /**
     * Get list of published assignments for students
     * @param assignmentState - String
     * @return list of published assignments
     */
    public List<Assignment.LightWeight> getPublishedAssignments(String assignmentState){
        return assignmentRepo
                .findAssignmentByState(new AssignmentState(assignmentState)).stream()
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toList());
    }

}
