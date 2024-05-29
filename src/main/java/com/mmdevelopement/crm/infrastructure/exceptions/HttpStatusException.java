package com.mmdevelopement.crm.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public class HttpStatusException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpStatusException(String message) {
        super(message);
    }

    public HttpStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatusException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatusException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
