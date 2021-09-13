package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final RBuilder rBuilder;

    public ApiException(RBuilder rBuilder) {
        super();
        this.rBuilder = rBuilder;
    }
}
