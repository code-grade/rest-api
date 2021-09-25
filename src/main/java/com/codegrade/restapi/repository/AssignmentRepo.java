package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepo extends JpaRepository<Assignment, UUID> {
}
