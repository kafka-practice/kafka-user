package org.example.kafkauser.common.annotation.trace.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * OpenTelemetry 의 Tracing 을 설정하는 클래스
 * 해당 클래스가 있어야 Kafka 메시지 헤더에 Tracing 정보 주입 가능
 * */
@Configuration
public class TracingConfig {
//    @Value("${opentelemetry.tracer.exporter.otlp.endpoint}")
//    private String url;

    @Value("${otel.resource.attributes.service.name}")
    private String serviceName;

    /*
    * OtlpHttpSpanExporter 빈 생성
    * */
    @Bean
    public OpenTelemetry openTelemetry() {
        // OtlpHttpSpanExporter 객체를 생성하여 OTLP HTTP 엔드포인트에 데이터 전송 설정.
        // `url`은 OTLP Collector 엔드포인트 주소.
//        OtlpHttpSpanExporter spanExporter = OtlpHttpSpanExporter.builder()
//                .setEndpoint(url) // OTLP HTTP 수집기가 위치한 URL을 설정합니다.
//                .build();

        // SdkTracerProvider 생성. 이는 OpenTelemetry SDK 의 트레이싱 구성 요소로, SpanProcessor와 리소스를 설정.
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(Resource.create(io.opentelemetry.api.common.Attributes.of(
                        // 서비스의 이름을 설정하여 각 Span에 서비스 정보를 포함시킴
                        ResourceAttributes.SERVICE_NAME, serviceName)))
                // BatchSpanProcessor를 추가하여 Span을 일괄 처리하고 `spanExporter`로 전송.
//                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        // OpenTelemetrySdk 객체 생성. 이를 통해 OpenTelemetry 추적 및 컨텍스트 전파 기능 구성.
        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                // 위에서 생성한 tracerProvider 설정하여 트레이싱 활성화.
                .setTracerProvider(tracerProvider)
                // W3C Trace Context Propagator 설정하여 컨텍스트 전파 지원.
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .build();

        // 전역 OpenTelemetry 객체 설정.
        GlobalOpenTelemetry.set(openTelemetrySdk);

        return openTelemetrySdk;

    }

}
