package org.example.kafkauser.event.listener;

import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import org.example.kafkauser.common.annotation.trace.TraceOutboxEvent;
import org.example.kafkauser.event.outbox.OutboxEvent;
import org.example.kafkauser.port.in.UsersOutboxUseCase;
import org.example.kafkauser.port.out.KafkaProducerPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class UsersOutboxEventListener {
    private final UsersOutboxUseCase usersOutboxUseCase;
    private final KafkaProducerPort kafkaProducerPort;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @TraceOutboxEvent(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEvent(OutboxEvent outboxEvent) {
        // Outbox 테이블에 이벤트 저장
        usersOutboxUseCase.saveOutboxEvent(outboxEvent);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @TraceOutboxEvent(phase = TransactionPhase.AFTER_COMMIT)
    public void sendToKafka(OutboxEvent outboxEvent) {
        try {
            // 1. 메시지로 보낼 payload와 전송할 Kafka의 토픽 정보를 가져옵니다.
            Long message = outboxEvent.getPayload();
            String topic = usersOutboxUseCase.getKafkaTopic(outboxEvent);

            // 2. 추출한 토픽에 Kafka 메시지를 전송합니다.
            kafkaProducerPort.sendWithRetryWithoutKey(topic, String.valueOf(message), Context.current());

            // 3. Outbox 이벤트를 처리 대기 상태로 변경합니다.
            usersOutboxUseCase.markOutboxEventPending(outboxEvent);
        } catch (Exception e) {
            // exception: 예외 발생 시 Outbox 이벤트를 실패 상태로 변경하고 Span에 예외를 기록합니다.
            usersOutboxUseCase.markOutboxEventFailed(outboxEvent);
        }
    }
}
