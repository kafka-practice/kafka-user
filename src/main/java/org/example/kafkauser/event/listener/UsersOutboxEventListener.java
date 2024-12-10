package org.example.kafkauser.event.listener;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.common.annotation.trace.TraceOutboxEvent;
import org.example.kafkauser.event.outbox.OutboxEvent;
import org.example.kafkauser.port.in.UsersOutboxUseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class UsersOutboxEventListener {
    private final UsersOutboxUseCase usersOutboxUseCase;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @TraceOutboxEvent(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEvent(OutboxEvent outboxEvent) {
        // Outbox 테이블에 이벤트 저장
        usersOutboxUseCase.saveOutboxEvent(outboxEvent);
    }
}
