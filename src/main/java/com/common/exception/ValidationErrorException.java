package com.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationErrorException extends CustomRuntimeException {
    public ValidationErrorException(String message) {
        super(HttpStatus.CONFLICT, "Invalid Validation Type Request", message);
    }
}
