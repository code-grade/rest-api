package com.codegrade.restapi.service;

import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.repository.SubmissionRepo;
import com.codegrade.restapi.repository.UserRepo;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class SubmissionService {

    private final SubmissionRepo submissionRepo;
    private final QuestionRepo questionRepo;
    private final AssignmentRepo assignmentRepo;
    private final UserRepo userRepo;

    // 1 make submission

    // 2 get all submissions by student+assignment+question

    // 3 evaluate student submission


}
