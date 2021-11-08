package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.*;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.AssignmentRepo;
import com.codegrade.restapi.repository.FinalSubmissionRepo;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.runtime.ExecOutput;
import com.codegrade.restapi.utils.RBuilder;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
public class PlagiarismService {

    private final RestTemplate restTemplate;
    private final FinalSubmissionRepo finalSubmissionRepo;
    private final AssignmentRepo assignmentRepo;
    private final QuestionRepo questionRepo;

    @Data
    static public class PlagiarismReportRequest {
        private String assignmentId;
        private String questionId;
        private Map<String, SourceCode> sources;

        public PlagiarismReportRequest(Assignment a, Question q, Map<String, SourceCode> s) {
            this.assignmentId = a.getAssignmentId().toString();
            this.questionId = q.getQuestionId().toString();
            this.sources = s;
        }
    }

    @Data
    static public class ReportEntry {
        private String first;
        private String second;
        private Long percentage;
        private Long lines;
    }

    @Data
    @AllArgsConstructor
    static public class ReportEntryX {
        private FinalSubmission.WithUser first;
        private FinalSubmission.WithUser second;
        private Long percentage;
        private Long lines;

        public ReportEntryX(FinalSubmission fs, FinalSubmission ss, Long p, Long l) {
            this.first = FinalSubmission.WithUser.fromFinalSubmission(fs);
            this.second = FinalSubmission.WithUser.fromFinalSubmission(ss);
            this.percentage = p;
            this.lines = l;
        }
    }

    @Data
    static public class PlagiarismReportResponse {
        private String message;
        private List<ReportEntry> data;
    }

    private List<ReportEntry> generatePlagiarismReport(PlagiarismReportRequest request) {
        var response = restTemplate.postForObject(
                "http://plagiarism/api/plagiarism/report",
                request,
                PlagiarismReportResponse.class
        );
        if (response == null) throw new ApiException(RBuilder.error("plagiarism server didn't respond"));
        return response.getData();
    }

    public List<ReportEntryX> generatePlagiarismReport(UUID assignmentId, UUID questionId) {
        var assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("assignment not found")));
        var question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ApiException(RBuilder.notFound("question not found")));

        var submissions = finalSubmissionRepo.findAllByAssignmentAndQuestion(assignment, question);
        var sources = submissions.stream().collect(Collectors.toMap(
                sub -> sub.getSubmissionId().toString(),
                FinalSubmission::getSourceCode
        ));

        return generatePlagiarismReport(new PlagiarismReportRequest(assignment, question, sources))
                .stream()
                .map(ent -> new ReportEntryX(
                        finalSubmissionRepo.findById(UUID.fromString(ent.getFirst())).get(),
                        finalSubmissionRepo.findById(UUID.fromString(ent.getSecond())).get(),
                        ent.getPercentage(),
                        ent.getLines()
                ))
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResRtCodeRun {
        private ExecOutput data;
        private String message;
    }
}
