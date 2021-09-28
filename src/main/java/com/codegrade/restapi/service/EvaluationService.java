package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.repository.SubmissionRepo;
import com.codegrade.restapi.runtime.ExecOutput;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EvaluationService {

    private final SubmissionRepo submissionRepo;
    private final QuestionRepo questionRepo;
    private final RuntimeService runtimeService;

    public TestCaseState checkTestCasePass(SourceCode sourceCode, String input, String output, Double timeLimit) {
        try {
            var result = runtimeService.runCode(sourceCode, input, timeLimit);
            switch (result.getStatus()) {
                case SUCCESS:
                    if (output.trim().equals(result.getOutput().trim())) {
                        return TestCaseState.CORRECT_OUTPUT;
                    } else {
                        return TestCaseState.INCORRECT_OUTPUT;
                    }
                case COMPILE:
                case RUNTIME:
                    return TestCaseState.RUNTIME_ERROR;
                case TIMEOUT:
                    return TestCaseState.TIMEOUT;

            }
        } catch (RuntimeException e) {
            return TestCaseState.UNKNOWN;
        }
        return TestCaseState.UNKNOWN;
    }

    @Async
    public void evaluate(Submission submission) {
        SubmissionResult result = submission.getResult();
        if (result.getEvaluated()) return;

        List<TestCase> testCases = submission.getQuestion().getTestCases();
        List<TestCaseResult> testCaseResultList = testCases.stream().map(t -> {
            TestCaseState state = checkTestCasePass(submission.getSourceCode(), t.getInput(),
                    t.getOutput(), t.getTimeLimit());
            return TestCaseResult.builder()
                    .id(t.getId())
                    .points((state == TestCaseState.CORRECT_OUTPUT)? t.getPoints(): 0)
                    .state(state)
                    .build();
        }).collect(Collectors.toList());

        // save to results
        result.setTestCases(testCaseResultList);
        result.setTotalPoints(testCaseResultList.stream()
                .map(TestCaseResult::getPoints)
                .mapToInt(Integer::intValue)
                .sum()
        );
        result.setEvaluatedTime(new Date());
        result.setEvaluated(true);

        // save to submission
        submission.setResult(result);
        submissionRepo.save(submission);
    }

}
