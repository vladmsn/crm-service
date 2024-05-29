package com.mmdevelopement.crm.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpStatusException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
