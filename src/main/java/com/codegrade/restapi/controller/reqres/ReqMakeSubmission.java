package com.codegrade.restapi.controller.reqres;

import com.codegrade.restapi.entity.SourceCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqMakeSubmission {

    private String assignmentId;
    private String questionId;
    private String sourceCode;
    private String language;

    public UUID getAssignmentId() {
        return UUID.fromString(assignmentId);
    }

    public UUID getQuestionId() {
        return UUID.fromString(questionId);
    }

    public SourceCode getSourceCode() {
        return SourceCode.builder()
                .source(sourceCode)
                .language(language)
                .build();
    }
}
