package org.example.kafkauser.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kafkauser.adapter.out.persistence.entity.constant.MessageStatus;
import org.example.kafkauser.common.entity.BaseEntity;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "outbox")
@Where(clause = "use_yn = true")
public class UsersOutboxEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", nullable = false)
    private Long payload;

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", nullable = false)
    private MessageStatus messageStatus;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersOutboxEntity that)) return false;
        return outboxId != null && Objects.equals(getOutboxId(), that.getOutboxId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOutboxId());
    }
}
