package org.example.kafkauser.adapter.out.persistence.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {
    PENDING("PENDING", "대기"),
    // Kafka 메시지 발생 확인 완료 전까지 대기 처리
    PROCESSED("PROCESSED", "Kafka 메시지 발행 처리완료"),
    // InternalKafkaListener를 통해 Kafka에 발행이 완료됨을 처리함
    SUCCESS("SUCCESS", "성공"),
    // gRPC 처리가 완료되면 Success 처리
    FAIL("FAIL", "실패");


    private final String code;
    private final String description;
}
