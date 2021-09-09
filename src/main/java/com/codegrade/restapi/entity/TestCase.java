package com.codegrade.restapi.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase implements Serializable {
    private long id;
    private String input;
    private String output;
}