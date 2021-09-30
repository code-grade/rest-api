package com.codegrade.restapi.controller.reqres;

import com.codegrade.restapi.entity.TestCaseState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResItemSampleTestCase {
    private Long id;
    private String input;
    private String output;
    private String expectedOutput;
    private TestCaseState state;
}
