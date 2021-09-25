package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.Assignment;
import com.codegrade.restapi.repository.AssignmentRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class AssignmentService {

    private AssignmentRepo assignmentRepo;

    public Assignment create(Assignment assignment) {
       return assignmentRepo.save(assignment);
    }
}
