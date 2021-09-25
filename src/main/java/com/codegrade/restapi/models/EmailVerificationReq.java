package com.codegrade.restapi.models;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Data
public class EmailVerificationReq {

    @NotNull
    @NotBlank
    private String token;

}
