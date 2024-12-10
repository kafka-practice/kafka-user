package org.example.kafkauser.event.outbox;

public interface OutboxEvent {
    Long getPayload();
    String getEventType();
}