package io.github.kkw.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {
    // handle exceptions from Spring, e.g. wrong email format
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestError> handleConstraintViolation(final ConstraintViolationException e) {
        return new ResponseEntity<>(
                new RestError(e.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestError> handleRestExceptions(final RestException e) {
        return new ResponseEntity<>(
                new RestError(e.getMessage(), e.getStatus()),
                e.getStatus());
    }
}
