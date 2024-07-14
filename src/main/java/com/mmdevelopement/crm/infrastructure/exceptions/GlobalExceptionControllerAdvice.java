package com.mmdevelopement.crm.infrastructure.exceptions;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionControllerAdvice.class);

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<Object> handleHttpStatusException(HttpStatusException ex) {
        logger.error("HttpStatusException occurred: {} - Status: {}", ex.getMessage(), ex.getHttpStatus().value());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), ex.getHttpStatus().value()), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        logger.error("An unexpected error occurred", ex); // This will log the stack trace as well
        return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    private static class ErrorResponse {
        private final String message;
        private final int status;
        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }
    }
}