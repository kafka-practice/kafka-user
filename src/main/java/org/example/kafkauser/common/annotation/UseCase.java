package org.example.kafkauser.common.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
// ElementType.TYPE : 클래스, 인터페이스(애노테이션 포함), 열거형에 적용 가능
@Retention(RetentionPolicy.RUNTIME)
// RetentionPolicy.RUNTIME : 실행 시간 동안에도 어노테이션 정보 유지됨
//                           -> 리플렉션 등을 통해 접근 가능
@Service
public @interface UseCase {
    String value() default "";
}
