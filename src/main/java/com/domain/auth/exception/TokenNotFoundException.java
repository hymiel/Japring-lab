package com.domain.auth.exception;

import com.common.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends CustomRuntimeException {
    public TokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Access Token Not Found Exception", "요청 인증 정보를 찾을 수 없습니다.");
    }
}
