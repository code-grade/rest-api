package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.PlagiarismReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlagiarismReportRepo extends JpaRepository<PlagiarismReport, UUID> {
}