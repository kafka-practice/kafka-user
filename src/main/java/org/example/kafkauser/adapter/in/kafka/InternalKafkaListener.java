package org.example.kafkauser.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkauser.common.annotation.trace.TraceOutboxEvent;
import org.example.kafkauser.common.annotation.trace.TraceOutboxKafka;
import org.example.kafkauser.event.UsersSignupEvent;
import org.example.kafkauser.event.outbox.OutboxEvent;
import org.example.kafkauser.port.in.UsersOutboxUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalKafkaListener {
    private final UsersOutboxUseCase usersOutboxUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @TraceOutboxKafka
    @KafkaListener(
            topics = "users-signup-outbox",
//            topicPartitions = @TopicPartition(
//                    topic = "users-signup-outbox",
//                    partitions = {"0", "1" ,"2"} // 특정 파티션 선택
//            ),
            groupId = "user-group-user-signup",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternalUsersSignup(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. Record 값을 이벤트 객체로 변환
        String jsonValue = record.value();
        System.out.println(jsonValue);
        OutboxEvent event = objectMapper.readValue(jsonValue, UsersSignupEvent.class);

        // 2. outbox 테이블에 이벤트 처리 상태 업데이트
        usersOutboxUseCase.markOutboxEventProcessed(event);

        // 3. ACK 처리
        acknowledgment.acknowledge();
        // 동일한 메시지가 다시 전달되지 않도록 방지(재처리 방지)
    }
}
