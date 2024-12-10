package org.example.kafkauser.dto.jpa;

import lombok.*;
import org.example.kafkauser.adapter.out.persistence.entity.constant.MessageStatus;
import org.example.kafkauser.common.exception.ErrorCode;
import org.example.kafkauser.common.exception.UsersException;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UsersOutboxDto {
    private Long id;
    private String eventType;   // 토픽정보 ex.MemberCreatedEvent
    private Long payload;       // 이벤트 내부의 id 필드를 저장. ex) memberId: 1L
    private String traceId;     // Kafka 메시지 처리 시, traceId
    private MessageStatus status;      // Kafka 메시지 처리 상태 (예: PENDING, PROCESSED, SUCCESS, FAIL)
    private LocalDateTime processedAt; // Kafka 메시지 처리 시간 (처리된 경우)

    // factory method
    public static UsersOutboxDto of(String eventType, Long payload, String nowTraceId, MessageStatus status) {
        return UsersOutboxDto.builder()
                .eventType(eventType)
                .payload(payload)
                .traceId(nowTraceId)
                .status(status)
                .build();
    }

    public void changeStatus(MessageStatus status) {
        if (ObjectUtils.isEmpty(status)) {
            throw new UsersException(ErrorCode.OUTBOX_STATUS_NOT_FOUND);
        }
        this.status = status;
    }

    public void changeProcessedAt(LocalDateTime now) {
        if (ObjectUtils.isEmpty(now)) {
            throw new UsersException(ErrorCode.OUTBOX_PROCESSED_AT_NOT_FOUND);
        }
        this.processedAt = now;
    }
}
