package com.codegrade.restapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptHandler {

    @ExceptionHandler(value = {APIException.class})
    public ResponseEntity<Map<String, Object>> apiExceptionHandler(APIException exception) {
        return exception.getRBuilder().compactResponse();
    }
}
