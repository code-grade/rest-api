package com.codegrade.restapi.service;

import com.codegrade.restapi.controller.reqres.ResItemSampleTestCase;
import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.repository.SubmissionRepo;
import com.codegrade.restapi.runtime.ExecOutput;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Test;
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

    private static TestCaseState toTestCaseState(ExecOutput res, String output) {
        switch (res.getStatus()) {
            case SUCCESS:
                if (output.trim().equals(res.getOutput().trim())) {
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
        return TestCaseState.UNKNOWN;
    }


    private TestCaseState checkTestCasePass(SourceCode sourceCode, String input, String output, Double timeLimit) {
        try {
            var result = runtimeService.runCode(sourceCode, input, timeLimit);
            return toTestCaseState(result, output);
        } catch (RuntimeException e) {
            return TestCaseState.UNKNOWN;
        }
    }


    public List<ResItemSampleTestCase> evaluationTest(SourceCode source, UUID questionId) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("invalid question id")));
        return question.getTestCases().parallelStream()
                .filter(TestCase::getSample)
                .map(t -> {
                    try {
                        ExecOutput result = runtimeService.runCode(source, t.getInput(), t.getTimeLimit());
                        return new ResItemSampleTestCase(
                                t.getId(),
                                t.getInput(),
                                result.getOutput(),
                                t.getOutput(),
                                toTestCaseState(result, t.getOutput())
                        );
                    } catch (RuntimeException e) {
                        return new ResItemSampleTestCase(
                                t.getId(),
                                t.getInput(),
                                null,
                                t.getOutput(),
                                TestCaseState.UNKNOWN
                        );
                    }
                }).collect(Collectors.toList());
    }

    @Async
    public void evaluate(Submission submission) {
        SubmissionResult result = submission.getResult();
        if (result.getEvaluated()) return;

        List<TestCase> testCases = submission.getQuestion().getTestCases();
        List<TestCaseResult> testCaseResultList = testCases.parallelStream().map(t -> {
            TestCaseState state = checkTestCasePass(submission.getSourceCode(), t.getInput(),
                    t.getOutput(), t.getTimeLimit());
            return TestCaseResult.builder()
                    .id(t.getId())
                    .points((state == TestCaseState.CORRECT_OUTPUT) ? t.getPoints() : 0)
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
