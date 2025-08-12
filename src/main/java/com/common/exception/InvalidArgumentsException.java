package com.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidArgumentsException extends CustomRuntimeException {
    public InvalidArgumentsException(String message) {
        super(HttpStatus.BAD_REQUEST, "Invalid Arguments Exception", message);
    }
}
