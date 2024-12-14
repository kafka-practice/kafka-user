package org.example.kafkauser.service.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.kafkauser.common.annotation.UseCase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestKafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToKafka() {
        String message = "Hello Kafka!";
        String topic = "users-signup-outbox";

        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
