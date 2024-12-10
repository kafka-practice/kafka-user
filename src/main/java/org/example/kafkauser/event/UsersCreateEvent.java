package org.example.kafkauser.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.kafkauser.event.outbox.OutboxEvent;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersCreateEvent implements OutboxEvent {
    private Long payload;

    @Override
    public Long getPayload() {
        return payload;
    }

    @Override
    public String getEventType() {
        return ExternalEventType.USERS_SIGNUP_OUTBOX.getType();
    }
}
