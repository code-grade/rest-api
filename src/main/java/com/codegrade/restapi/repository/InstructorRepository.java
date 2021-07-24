package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
}
