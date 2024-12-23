package org.example.kafkauser.port.out;

import org.springframework.kafka.support.SendResult;
import io.opentelemetry.context.Context;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerPort {
    CompletableFuture<SendResult<String, String>> sendMessageWithoutKey(
            String topic, String payloadJson, Context context
    );

    CompletableFuture<SendResult<String, String>> sendMessageWithKey(
            String topic, String key, String payloadJson, Context context
    );

    void sendWithRetryWithoutKey(
            String topic, String payloadJson, Context context
    );

    void sendWithRetryWithKey(
            String topic, String key, String payloadJson, Context context
    );
}
