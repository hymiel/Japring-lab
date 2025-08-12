package com.common.exception;


import org.springframework.http.HttpStatus;

public class JsonParsingErrorException extends CustomRuntimeException {
    public JsonParsingErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Json Parsing Error Exception", message);
    }
}
