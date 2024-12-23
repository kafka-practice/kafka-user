package org.example.kafkauser.common.annotation.trace.aop;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.kafkauser.common.annotation.trace.TraceOutboxEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
public class TraceOutboxEventAspect {
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("spring-event");

    /*
     * Outbox 이벤트 리스너 메서드 호출 시 Span을 생성하고 종료합니다.
     *
     * @param joinPoint - 프록시 대상 메서드
     * @return Object - 프록시 대상 메서드의 반환값
     * @throws Throwable - 예외
     */
    @Around(value = "@annotation(org.example.kafkauser.common.annotation.trace.TraceOutboxEvent)")
    public Object traceOutboxEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 정보 추출
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // @TraceOutboxEvent 어노테이션 정보 추출
        TraceOutboxEvent annotation = method.getAnnotation(TraceOutboxEvent.class);
        TransactionPhase phase = annotation.phase();

        // 트랜잭션 단계에 따라 Span 이름 설정
        String spanName = determineSpanName(phase);

        // Span 생성
        Span span = tracer.spanBuilder(spanName).startSpan();

        // Span을 현재 컨텍스트로 설정
        try (Scope scope = span.makeCurrent()) { // 현재 스레드에 Span을 연결하여 추적 정보 전파 가능하게 함
            // 실제 메서드 실행
            return joinPoint.proceed(); // AOP로 감싼 실제 비즈니스 로직을 실행
        } catch (Exception e) {
            // 예외 발생 시 Span에 기록
            span.recordException(e);
            log.error("Exception in Outbox Event Listener: {}", e.getMessage(), e);
            throw e; // 예외를 다시 던져서 호출자에게 알립니다.
        } finally {
            // Span 종료
            log.info("Ending traceId: {}, spanId: {}", span.getSpanContext().getTraceId(), span.getSpanContext().getSpanId());
            span.end();
        }
    }

    private String determineSpanName(TransactionPhase phase) {
        if (phase == TransactionPhase.BEFORE_COMMIT) {
            return "Content [spring-event] before-commit (outbox-table save)";
        } else if (phase == TransactionPhase.AFTER_COMMIT) {
            return "Content [spring-event] after-commit (kafka-produce && outbox table process)";
        } else {
            return "Content [spring-event] outbox processing";
        }
    }
}
