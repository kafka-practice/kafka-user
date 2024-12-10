package org.example.kafkauser.service.outbox;

import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import org.example.kafkauser.adapter.out.persistence.entity.constant.MessageStatus;
import org.example.kafkauser.common.annotation.UseCase;
import org.example.kafkauser.dto.jpa.UsersOutboxDto;
import org.example.kafkauser.event.outbox.OutboxEvent;
import org.example.kafkauser.port.in.UsersOutboxUseCase;
import org.example.kafkauser.port.out.UsersOutboxCrudPort;

import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
public class UsersOutboxService implements UsersOutboxUseCase {
    private final UsersOutboxCrudPort usersOutboxCrudPort;

    @Override
    public Long saveOutboxEvent(OutboxEvent outboxEvent) {
        Span currentSpan = Span.current();
        String nowTraceId = currentSpan.getSpanContext().getTraceId();
        String eventType = getKafkaTopic(outboxEvent);
        UsersOutboxDto usersOutboxDto =
                UsersOutboxDto.of(eventType, outboxEvent.getPayload(), nowTraceId, MessageStatus.PENDING);

        return usersOutboxCrudPort.saveUsersOutboxEvent(usersOutboxDto);
    }

    @Override
    public void markOutboxEventPending(OutboxEvent outboxEvent) {
        // 1. EventType에 따라 적절한 토픽 이름 반환
        String eventType = getKafkaTopic(outboxEvent);
        // 2. OutboxEvent 찾기
        UsersOutboxDto usersOutboxDto = usersOutboxCrudPort.findUsersOutboxBy(outboxEvent.getPayload(), eventType);
        // 3. OutboxEvent status를 Pending으로 변경
        usersOutboxDto.changeStatus(MessageStatus.PENDING);
        usersOutboxDto.changeProcessedAt(LocalDateTime.now());
        usersOutboxCrudPort.saveUsersOutboxEvent(usersOutboxDto);
    }

    @Override
    public void markOutboxEventProcessed(OutboxEvent outboxEvent) {
        // 1. EventType에 따라 적절한 토픽 이름 반환
        String eventType = getKafkaTopic(outboxEvent);
        // 2. OutboxEvent 찾기
        UsersOutboxDto usersOutboxDto = usersOutboxCrudPort.findUsersOutboxBy(outboxEvent.getPayload(), eventType);
        // 3. OutboxEvent status를 Processed으로 변경
        usersOutboxDto.changeStatus(MessageStatus.PROCESSED);
        usersOutboxDto.changeProcessedAt(LocalDateTime.now());
        usersOutboxCrudPort.saveUsersOutboxEvent(usersOutboxDto);
    }

    @Override
    public void markOutboxEventSuccess(OutboxEvent outboxEvent) {
        // 1. EventType에 따라 적절한 토픽 이름 반환
        String eventType = getKafkaTopic(outboxEvent);
        // 2. OutboxEvent 찾기
        UsersOutboxDto usersOutboxDto = usersOutboxCrudPort.findUsersOutboxBy(outboxEvent.getPayload(), eventType);
        // 3. OutboxEvent status를 Success으로 변경
        usersOutboxDto.changeStatus(MessageStatus.SUCCESS);
        usersOutboxDto.changeProcessedAt(LocalDateTime.now());
        usersOutboxCrudPort.saveUsersOutboxEvent(usersOutboxDto);
    }

    @Override
    public void markOutboxEventFailed(OutboxEvent outboxEvent) {
        // 1. EventType에 따라 적절한 토픽 이름 반환
        String eventType = getKafkaTopic(outboxEvent);
        // 2. OutboxEvent 찾기
        UsersOutboxDto usersOutboxDto = usersOutboxCrudPort.findUsersOutboxBy(outboxEvent.getPayload(), eventType);
        // 3. OutboxEvent status를 Fail로 변경
        usersOutboxDto.changeStatus(MessageStatus.FAIL);
        usersOutboxDto.changeProcessedAt(LocalDateTime.now());
        usersOutboxCrudPort.saveUsersOutboxEvent(usersOutboxDto);
    }

    @Override
    public String getKafkaTopic(OutboxEvent outboxEvent) {
        return outboxEvent.getEventType();
    }
}
