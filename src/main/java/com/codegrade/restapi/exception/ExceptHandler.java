package com.codegrade.restapi.exception;

import com.codegrade.restapi.utils.RBuilder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Order(Ordered.HIGHEST_PRECEDENCE)
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
                .setData("errors", ex.getBindingResult().getAllErrors().stream()
                        .collect(Collectors.toMap(
                                error -> ((FieldError) error).getField(),
                                error -> Optional.ofNullable(error.getDefaultMessage())
                                        .orElse("invalid")
                        )))
                .compactResponse();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        return RBuilder.badRequest()
                .setMsg("invalid request")
                .setData("errors",
                        ex.getConstraintViolations().stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toMap(
                                        cv -> StreamSupport.stream(
                                                        cv.getPropertyPath().spliterator(),
                                                        false
                                                )
                                                .map(Path.Node::getName)
                                                .reduce((first, second) -> second)
                                                .orElseGet(() -> cv.getPropertyPath().toString()),
                                        ConstraintViolation::getMessage
                                ))
                )
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
