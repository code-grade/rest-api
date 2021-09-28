package com.codegrade.restapi.runtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ReqRunCode {
    @NotNull
    private String source;

    @NotNull
    private String input;
    private Double timeLimit;
}
