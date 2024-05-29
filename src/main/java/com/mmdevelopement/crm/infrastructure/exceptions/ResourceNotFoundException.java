package com.mmdevelopement.crm.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HttpStatusException {

        public ResourceNotFoundException(String message) {
            super(message, HttpStatus.NOT_FOUND);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, HttpStatus.NOT_FOUND, cause);
        }
}
