package com.codegrade.restapi.service;

import com.codegrade.restapi.controller.reqres.ResItemPublicAssignment;
import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.ParticipationRepo;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.repository.UserRepo;
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
    private final UserRepo userRepo;
    private final JobService jobService;

    /**
     * Create new assignment
     *
     * @param instructor - Instructor
     * @param assignment - Assignment Details
     * @return Assignment with complete details
     */
    public Assignment.LightWeight createAssignment(User instructor, Assignment assignment, List<UUID> questions) {
        assignment.setInstructor(instructor);
        assignment.setQuestions(questions.stream()
                .map(id -> questionRepo.findById(id)
                        .orElseThrow(() -> new ApiException(RBuilder.badRequest("invalid question id " + id))))
                .collect(Collectors.toSet()));
        assignment.setState(AssignmentState.DRAFT);
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
    public List<Assignment.LightWeight> getByInstructor(User instructor, AssignmentState assignmentState) {
        return assignmentRepo
                .findAssignmentByInstructorAndState(instructor, assignmentState).stream()
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toList());
    }

    /**
     * Get assignment by id
     *
     * @param assignmentId - UUID
     * @return assignment details
     */
    public Assignment.WithQuestions getAssignmentById(UUID assignmentId) {
        return assignmentRepo.findById(assignmentId)
                .map(Assignment.WithQuestions::fromAssignment)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
    }


    /**
     * Enroll to an assignment
     *
     * @param assignmentId - UUID
     * @param student      - User
     */
    public Boolean enrollToAssignment(UUID assignmentId, User student) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));

        if (!assignment.getState().equals(AssignmentState.PUBLISHED) && !assignment.getState().equals(AssignmentState.DRAFT)) {
            throw new ApiException(RBuilder.locked("cannot change enrollment at this stage"));
        }

        var pid = new Participation.ParticipationId(assignmentId, student.getUserId());
        if (participationRepo.findById(pid).isPresent()) {
            throw new ApiException(RBuilder.badRequest("already enrolled"));
        } else {
            Participation part = Participation.builder()
                    .assignment(assignment)
                    .user(student)
                    .enrollmentDate(new Date())
                    .build();
            participationRepo.save(part);
            return true;
        }
    }

    /**
     * Un-enroll student from an assignment
     *
     * @param assignmentId - UUID
     * @param student      - Student
     */
    public Boolean unEnrollFromAssignment(UUID assignmentId, User student) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));

        if (!assignment.getState().equals(AssignmentState.PUBLISHED) &&
                !assignment.getState().equals(AssignmentState.DRAFT)) {
            throw new ApiException(RBuilder.locked("cannot change enrollment at this stage"));
        }

        Participation p = participationRepo.findById(new Participation.ParticipationId(
                student.getUserId(), assignmentId
        )).orElseThrow(() -> new ApiException(RBuilder.notFound("you haven't enrolled")));
        participationRepo.delete(p);
        return true;
    }


    /**
     * Get participated assignments by user
     *
     * @param student - User
     * @return List of assignments
     */
    public Set<Assignment.LightWeight> getAssignmentByStudent(User student) {
        return participationRepo.findParticipationByUser(student).stream()
                .map(Participation::getAssignment)
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toSet());
    }

    public Set<Assignment.LightWeight> getAssignmentByStudent(User student, AssignmentState state) {
        return participationRepo.findParticipationByUser(student).stream()
                .map(Participation::getAssignment)
                .filter(as -> as.getState() == state)
                .map(Assignment.LightWeight::fromAssignment).collect(Collectors.toSet());
    }


    /**
     * Get list of participants by assignmentId
     *
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
     * Change assignment state
     *
     * @param assignmentId - UUID
     * @param state        - state
     * @return - Assignment
     */
    public Assignment.LightWeight changeAssignmentState(UUID assignmentId, AssignmentState state) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        assignment.setState(state);
        if (state.equals(AssignmentState.PUBLISHED)) {
            jobService.scheduleAssignment(assignmentId);
        }
        return Assignment.LightWeight.fromAssignment(assignmentRepo.save(assignment));
    }

    /**
     * Update assignment details
     *
     * @param assignmentId - UUID
     * @param data         - Assignment Data
     * @param questionIds  - Questions
     * @return - Assignment
     */
    public Assignment.WithQuestions updateAssignment(UUID assignmentId, Assignment data, List<UUID> questionIds) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        Set<Question> questions = questionIds.stream()
                .map(qid -> questionRepo.findById(qid)
                        .orElseThrow(() -> new ApiException(
                                RBuilder.notFound("invalid question id " + qid))
                        )).collect(Collectors.toSet());

        if (data.getTitle() != null) assignment.setTitle(data.getTitle());
        if (data.getDescription() != null) assignment.setDescription(data.getDescription());
        if (!questions.isEmpty()) assignment.setQuestions(questions);

        // schedule update
        if (data.getSchedule().isValid()) assignment.setSchedule(data.getSchedule());
        return Assignment.WithQuestions.fromAssignment(assignmentRepo.save(assignment));
    }

    /**
     * Get all public assignments
     *
     * @param assignmentState - assignment state
     * @return Assignments
     */
    public List<ResItemPublicAssignment> getPublicAssignments(AssignmentState assignmentState, User student) {
        Set<Participation> participation = participationRepo.findParticipationByUser(student);
        return assignmentRepo.findAssignmentByTypeAndState(AssignmentType.PUBLIC, assignmentState).stream()
                .map(a -> ResItemPublicAssignment.fromAssignment(a, participation.stream()
                                .anyMatch(p -> p.getAssignment().equals(a) && p.getUser().equals(student))))
                .collect(Collectors.toList());
    }

    public List<ResItemPublicAssignment> getPublicAssignments(User student) {
        Set<Participation> participation = participationRepo.findParticipationByUser(student);
        return assignmentRepo.findAssignmentByType(AssignmentType.PUBLIC).stream()
                .map(a -> ResItemPublicAssignment.fromAssignment(a, participation.stream()
                        .anyMatch(p -> p.getAssignment().equals(a) && p.getUser().equals(student))))
                .collect(Collectors.toList());
    }

    /**
     * Make final grade
     *
     * @param assignmentId - UUID
     * @param studentId    - UUID
     * @param finalGrade   - Final Grade
     * @return - Participation
     */
    public Participation.LightWeight submitFinalGrade(UUID assignmentId, UUID studentId, FinalGrade finalGrade) {
        var pid = Participation.ParticipationId.fromIds(studentId, assignmentId);
        Participation participation = participationRepo.findById(pid)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid participation")));
        finalGrade.setGradedTime(new Date());
        participation.setFinalGrade(finalGrade);
        return Participation.LightWeight.fromParticipation(participationRepo.save(participation));
    }

    public FinalGrade getFinalGrade(UUID assignmentId, UUID studentId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid student id")));
        return getFinalGrade(assignmentId, student);
    }
    public FinalGrade getFinalGrade(UUID assignmentId, User student) {
        var pid = Participation.ParticipationId.fromIds(student.getUserId(), assignmentId);
        Participation participation = participationRepo.findById(pid)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid participation")));
        FinalGrade finalGrade = participation.getFinalGrade();
        if (finalGrade == null || finalGrade.getFinalGrade() == null) {
            throw new ApiException(RBuilder.notFound("haven't graded yet"));
        }
        return finalGrade;
    }
}
