package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<Map<String, Object>> apiExceptionHandler(ApiException ex) {
        return ex.getRBuilder().compactResponse();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return RBuilder.badRequest()
                .setMsg("invalid request")
                .setData(ex.getBindingResult().getAllErrors().stream()
                        .collect(Collectors.toMap(
                                error -> ((FieldError) error).getField(),
                                error -> Optional.ofNullable(error.getDefaultMessage())
                                        .orElse("invalid")
                        )))
                .compactResponse();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(HttpMessageNotReadableException ex) {

        return RBuilder.badRequest()
                .setMsg("invalid request")
                .setData("error", ex.getMessage())
                .compactResponse();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedExceptions(AccessDeniedException ex) {

        return RBuilder.unauthorized()
                .setMsg("have no authorization to access")
                .setData("error", ex.getMessage())
                .compactResponse();
    }
}
