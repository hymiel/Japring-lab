package com.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomRuntimeException extends RuntimeException{

    private final HttpStatus status;
    private final String reason;

    public CustomRuntimeException(HttpStatus status, String reason, String message) {
        super(message);
        this.status = status;
        this.reason = reason;
    }
}
