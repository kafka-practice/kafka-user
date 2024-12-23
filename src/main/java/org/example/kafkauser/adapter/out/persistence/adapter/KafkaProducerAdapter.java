package org.example.kafkauser.adapter.out.persistence.adapter;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.kafkauser.common.annotation.MessagingAdapter;
import org.example.kafkauser.port.out.KafkaProducerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@MessagingAdapter
@RequiredArgsConstructor
public class KafkaProducerAdapter implements KafkaProducerPort {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("kafka-producer");

    // Kafka 메시지에 트레이스 컨텍스트를 주입하기 위한 TextMapSetter
    private static final TextMapSetter<ProducerRecord<String, String>> setter =
            (carrier, key, value) -> carrier.headers().add(key, value.getBytes(StandardCharsets.UTF_8));

    @Override
    public CompletableFuture<SendResult<String, String>> sendMessageWithoutKey(
            String topic, String payloadJson, Context context
    ) {
        return sendMessageWithKey(topic, null, payloadJson, context);
    }

    @Override
    public CompletableFuture<SendResult<String, String>> sendMessageWithKey(
            String topic, String key, String payloadJson, Context context
    ) {
        // 1. OpenTelemetry Span 생성 및 설정
        Span span = tracer.spanBuilder("[kafka] : message-produce").setParent(context).startSpan();

        try(Scope scope = span.makeCurrent()) {
            // Kafka 메시지 레코드 생성
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payloadJson);

            // Tracerparent 헤더를 kafka 레코드에 주입하여 트레이스를 연결 (리스너에서 가져다 사용)
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(context, record, setter);

            // Kafka 메시지 전송
            return kafkaTemplate.send(record);
        } finally {
            // span 종료
            span.end();
        }
    }

    // 재시도 로직 --------------------------------------------------------
    @Override
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendWithRetryWithoutKey(
            String topic, String payloadJson, Context context
    ) {
        sendMessageWithoutKey(topic, payloadJson, context).whenComplete((result, ex) -> {
            handleSendResult(topic, payloadJson, ex, result);
        });
    }

    @Override
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendWithRetryWithKey(
            String topic, String key, String payloadJson, Context context
    ) {
        sendMessageWithKey(topic, key, payloadJson, context).whenComplete((result, ex) -> {
            handleSendResult(topic, payloadJson, ex, result);
        });
    }
    // ----------------------------------------------------------------

    // 메시지 전송 결과 처리
    private void handleSendResult(
            String topic, String payloadJson, Throwable ex, SendResult<String, String> result
    ) {
        if (ex != null) {
            log.error("Failed to send message to Kafka topic {}: {}", topic, ex.getMessage());
            // 알림 시스템을 통한 예외 처리 (slack, email, etc...)
            throw new RuntimeException("Failed to send message to Kafka topic ", ex);
        }

        // 성공 시 전송된 메시지의 오프셋을 로그에 기록
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("Send message=[{}] to topic=[{}] with offset=[{}]", payloadJson, topic, metadata.offset());
    }
}
