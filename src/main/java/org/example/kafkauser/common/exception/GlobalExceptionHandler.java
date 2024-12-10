package org.example.kafkauser.common.exception;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.dto.controller.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        ApiResponse<String> apiResponse = ApiResponse.fail(ErrorCode.UNEXPECTED_ERROR, e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UsersException.class)
    public ResponseEntity<ApiResponse<String>> handleUsersException(UsersException e) {
        ApiResponse<String> apiResponse = ApiResponse.fail(e.getErrorCode());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Spring Boot 애플리케이션에서 데이터 유효성 검사 중에 발생하는 제약 조건 위반 예외를 처리하기 위해 사용됩니다.
     * 이 예외는 주로 Java Bean Validation (JSR 380) 어노테이션(예: @NotNull, @Size, @Min 등)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        // 제약 조건 위반 예외에서 발생한 에러 메시지를 수집합니다.
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        // 유효성 검사 실패 시, 클라이언트에게 에러 메시지를 반환합니다.
        ApiResponse<String> response = ApiResponse.fail(ErrorCode.VALIDATION_FAILED, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
