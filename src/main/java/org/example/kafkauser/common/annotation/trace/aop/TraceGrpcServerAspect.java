package org.example.kafkauser.common.annotation.trace.aop;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.kafkauser.common.config.grpc.interceptor.GrpcMetadata;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TraceGrpcServerAspect {
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    /**/
    @Around("@annotation(org.example.kafkauser.common.annotation.trace.TraceGrpcServer)")
    public Object traceGrpcServer(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. gRPC Context 에서 OpenTelemetry Context 추출 (Interceptor 에서 traceId를 추출하여 세팅한 컨텍스트를 사용)
        io.opentelemetry.context.Context otelContext = GrpcMetadata.OTEL_CONTEXT_KEY.get();

        // 2. Span 생성 (생성한 otelContext 를 부모로 설정. (이렇게 수행 시, 부모 Span 의 traceId를 사용.)
        Span span = tracer.spanBuilder("User [grpc] server response")
                .setParent(otelContext)
                .startSpan();

        // 3. Span 을 현재 컨텍스트에 설정 후, 메서드 실행
        try (Scope scope = span.makeCurrent()) {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // 에러 발생 시, Span 에 기록
            span.recordException(e);
            throw e;
        } finally {
            // Span 종료
            span.end();
        }
    }
}
