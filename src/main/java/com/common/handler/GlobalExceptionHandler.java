package com.common.handler;

import com.common.dto.ResponseDto;
import com.common.exception.CustomRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ResponseDto> customRuntimeExceptionHandler(CustomRuntimeException e) {
        return new ResponseEntity<>(ResponseDto.fail(e.getStatus().value(),
                e.getReason(),
                e.getMessage()),
                e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ResponseDto.fail(HttpStatus.BAD_REQUEST.value(),
                "Validation Fail Exception",
                e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ResponseDto> jsonProcessingExceptionHandler(JsonProcessingException e) {
        return new ResponseEntity<>(ResponseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                "Json Serializer Fail Exception"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ResponseDto> HttpServerErrorExceptionHandler(HttpServerErrorException e) {
        return new ResponseEntity<>(ResponseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                "Http Server Error Exception"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(ResponseDto.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                "서버 에러가 발생했습니다. \nERROR CODE=[4000]"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
