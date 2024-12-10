package org.example.kafkauser.dto.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // JSON 직렬화 시 null 값인 필드를 제외하도록 설정
public class ErrorResponse {
    private String errorCode;
    private String message;
    private List<String> errors;

    // factory method
    // 장점 1. 객체 생성 과정 캡슐화 -> 클래스 내부에서만 객체 생성 가능
    //     2. 객체 생성의 일관성 유지
    //     3. 유연성 제공(다중 오버로드 등)
    //     4. 가독성 향상
    //     5. 클래스 변경 시 영향 최소화
    public static ErrorResponse of(String errorCode, String message, List<String> errors) {
        return new ErrorResponse(errorCode, message, errors);
    }
}
