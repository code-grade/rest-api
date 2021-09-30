package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.AssignmentState;
import com.codegrade.restapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, UUID> {

    List<Assignment> findAssignmentByInstructor(User instructor);
    List<Assignment> findAssignmentByInstructorAndState(User instructor, AssignmentState state);
    List<Assignment> findAssignmentByState(AssignmentState state);
}
