package com.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**공통 응답 객체**/
@Getter
@Builder
@AllArgsConstructor
public class ResponseDto<T> {
    private final Boolean ok;
    private final Integer status;
    private final String message;
    private final String reason;
    private final T data;

    public static <T> ResponseDto<T> ok(Integer status, T data) {
        return ResponseDto
                .<T>builder()
                .ok(true)
                .status(status)
                .message(null)
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> fail(Integer status, String reason, String message) {
        return ResponseDto
                .<T>builder()
                .ok(false)
                .status(status)
                .reason(reason)
                .message(message)
                .data(null)
                .build();
    }
}
