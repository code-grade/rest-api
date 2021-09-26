package com.codegrade.restapi.entity;

import lombok.Data;

@Data
public class TestCaseResult {
    private Long id;
    private Boolean passed;
    private Boolean points;
}
