package com.domain.user.exception;


import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomRuntimeException {
    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "User Not Found Exception", message);
    }
}
