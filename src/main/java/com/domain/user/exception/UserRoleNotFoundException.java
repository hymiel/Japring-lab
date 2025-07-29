package com.domain.user.exception;

import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UserRoleNotFoundException extends CustomRuntimeException {
    public UserRoleNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "User Role Not Found Exception", message);
    }
}
