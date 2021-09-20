package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ApiException extends AuthenticationException {

    private final RBuilder rBuilder;

    public static ApiException withRBuilder(RBuilder rBuilder) {
        return new ApiException(rBuilder);
    }

    public ApiException(RBuilder rBuilder) {
        super(rBuilder.getMessage());
        this.rBuilder = rBuilder;
    }




}
