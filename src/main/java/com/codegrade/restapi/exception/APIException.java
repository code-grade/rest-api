package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {

    private final RBuilder rBuilder;

    public APIException(RBuilder rBuilder) {
        super();
        this.rBuilder = rBuilder;
    }
}
