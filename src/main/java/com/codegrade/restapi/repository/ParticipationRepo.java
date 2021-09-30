package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.entity.Participation;
import com.codegrade.restapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ParticipationRepo extends JpaRepository<Participation, Participation.ParticipationId> {
    Set<Participation> findParticipationByUser(User student);
    Set<Participation> findParticipationByAssignment(Assignment assignment);
}
