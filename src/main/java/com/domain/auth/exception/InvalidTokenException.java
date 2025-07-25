package com.domain.auth.exception;

import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomRuntimeException {
    public InvalidTokenException(String message) {
        super(HttpStatus.CONFLICT, "Invalid Token Type Exception", message);
    }
}
