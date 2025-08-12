package com.common.exception;
import org.springframework.http.HttpStatus;

public class DatabaseResultException extends CustomRuntimeException {
    public DatabaseResultException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Database Result Error Exception", message);
    }
}
