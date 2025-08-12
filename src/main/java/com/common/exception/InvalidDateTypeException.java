package com.common.exception;
import org.springframework.http.HttpStatus;

public class InvalidDateTypeException extends CustomRuntimeException {
    public InvalidDateTypeException(String message) {
        super(HttpStatus.CONFLICT, "Invalid Date Type Request", message);
    }
}
