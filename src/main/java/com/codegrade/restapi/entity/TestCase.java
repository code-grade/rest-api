package com.codegrade.restapi.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase implements Serializable {
    private Long id;
    private String input;
    private String output;
    private Long timeLimit;
    private Integer points;
    private Boolean sample = false;
}