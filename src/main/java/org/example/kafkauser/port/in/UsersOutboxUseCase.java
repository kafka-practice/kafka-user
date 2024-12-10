package org.example.kafkauser.port.in;

import org.example.kafkauser.event.outbox.OutboxEvent;

public interface UsersOutboxUseCase {
    Long saveOutboxEvent(OutboxEvent outboxEvent);
    void markOutboxEventPending(OutboxEvent outboxEvent);
    void markOutboxEventProcessed(OutboxEvent outboxEvent);
    void markOutboxEventSuccess(OutboxEvent outboxEvent);
    void markOutboxEventFailed(OutboxEvent outboxEvent);
    String getKafkaTopic(OutboxEvent outboxEvent);
}