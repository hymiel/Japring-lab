package com.domain.auth.exception;

import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends CustomRuntimeException {
    public ExpiredTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, "Expired Token Exception", message);
    }
}
