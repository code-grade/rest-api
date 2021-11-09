package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.FinalSubmissionRepo;
import com.codegrade.restapi.repository.SubmissionRepo;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class JobService {

    private final JobScheduler jobScheduler;
    private final AssignmentRepo assignmentRepo;
    private final SubmissionRepo submissionRepo;
    private final FinalSubmissionRepo finalSubmissionRepo;

    //        ╦ ╦╔╦╗╦╦  ╔═╗
    //        ║ ║ ║ ║║  ╚═╗
    //        ╚═╝ ╩ ╩╩═╝╚═╝

    private void _updateFinalSubmission_Question(Assignment as, Question q) {
        as.getParticipants()
                .forEach(p -> {
                    Sort maximumPoints = Sort.by(Sort.Direction.DESC, "result.totalPoints");
                    submissionRepo
                            .findDistinctFirstByAssignmentAndUserAndQuestion(as, p.getUser(), q, maximumPoints)
                            .ifPresent(s -> finalSubmissionRepo.save(FinalSubmission.fromSubmission(s)));
                });
    }

    @Transactional
    public void updateFinalSubmission_forAssignment(Assignment assignment) {
        finalSubmissionRepo.deleteAllByAssignment(assignment);
        assignment.getQuestions()
                .forEach(q -> _updateFinalSubmission_Question(assignment, q));
    }

    //        ╔═╗╔═╗╔═╗╔╦╗╦ ╦╦═╗╔═╗╔═╗
    //        ╠╣ ║╣ ╠═╣ ║ ║ ║╠╦╝║╣ ╚═╗
    //        ╚  ╚═╝╩ ╩ ╩ ╚═╝╩╚═╚═╝╚═╝

    public ZonedDateTime toZonedTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC"));
    }

    public void openAssignment(UUID assignmentId, Integer scheduleJobId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        AssignmentSchedule schedule = assignment.getSchedule();
        if (assignment.getState().equals(AssignmentState.PUBLISHED) &&
                schedule.getIsScheduled() &&
                Objects.equals(schedule.getScheduleJobId(), scheduleJobId)) {
            assignment.setState(AssignmentState.OPEN);
            assignmentRepo.save(assignment);
        }
    }

    @Transactional
    public void closeAssignment(UUID assignmentId, Integer scheduleJobId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        AssignmentSchedule schedule = assignment.getSchedule();
        if (assignment.getState().equals(AssignmentState.OPEN) &&
                schedule.getIsScheduled() &&
                Objects.equals(schedule.getScheduleJobId(), scheduleJobId)) {
            assignment.setState(AssignmentState.CLOSED);
            assignmentRepo.save(assignment);
            updateFinalSubmission_forAssignment(assignment);
        }
    }

    public void scheduleAssignment(UUID assignmentId) {
       Assignment assignment = assignmentRepo.findById(assignmentId)
               .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        AssignmentSchedule schedule = assignment.getSchedule();
        log.info("Scheduling assignment "+ assignmentId);
        if (schedule.getIsScheduled()) {
            schedule.setScheduleJobId(schedule.getScheduleJobId() + 1);
            ZonedDateTime zonedOpenTime = toZonedTime(schedule.getOpenTime());
            ZonedDateTime zonedCloseTime = toZonedTime(schedule.getCloseTime());
            jobScheduler.schedule(zonedOpenTime,
                    () -> openAssignment(assignmentId, schedule.getScheduleJobId()));
            log.info("Assignment " + assignmentId + " opening at " + zonedOpenTime.toString());
            jobScheduler.schedule(zonedCloseTime,
                    () -> closeAssignment(assignmentId, schedule.getScheduleJobId()));
            log.info("Assignment " + assignmentId + " closing at " + zonedCloseTime.toString());
            assignment.setSchedule(schedule);
            assignmentRepo.save(assignment);
        }
    }

}
