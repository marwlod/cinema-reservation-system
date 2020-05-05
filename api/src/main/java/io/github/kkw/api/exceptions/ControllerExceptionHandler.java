package io.github.kkw.api.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    // handle exceptions from Spring, e.g. wrong email format
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody ResponseEntity<RestError> handleConstraintViolation(final ConstraintViolationException e) {
        logger.error("ConstraintViolationException was caught:", e);
        return new ResponseEntity<>(
                new RestError(e.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    public @ResponseBody ResponseEntity<RestError> handleRestExceptions(final RestException e) {
        logger.error("RestException was caught:", e);
        return new ResponseEntity<>(
                new RestError(e.getMessage(), e.getStatus()),
                e.getStatus());
    }
}
