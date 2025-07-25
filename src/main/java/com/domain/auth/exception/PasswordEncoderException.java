package com.domain.auth.exception;

import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class PasswordEncoderException extends CustomRuntimeException {
    public PasswordEncoderException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Password Encoder Module Error", message);
    }
}
