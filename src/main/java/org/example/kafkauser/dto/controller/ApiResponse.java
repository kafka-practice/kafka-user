package org.example.kafkauser.dto.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kafkauser.common.exception.ErrorCode;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ApiResponse<T> {
    private ResponseStatus success;
    private String message;
    private T data;
    private ErrorResponse error;

    // 메시지를 받지 않는 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                "Request was successful",
                data,
                null
        );
    }

    // 메시지를 받는 성공 응답
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS,
                message,
                data,
                null
        );
    }

    // 커스텀 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), null);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorResponse.getMessage(),
                null,
                errorResponse
        );
    }

    // 일반적인 예외 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), message, null);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                message,
                null,
                errorResponse
        );
    }

    // validate 전용 fail
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, List<String> errors) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), errors);

        return new ApiResponse<>(
                ResponseStatus.FAIL,
                errorResponse.getMessage(),
                null,
                errorResponse
        );
    }
}
