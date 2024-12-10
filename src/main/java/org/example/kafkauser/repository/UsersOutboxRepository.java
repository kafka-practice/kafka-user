package org.example.kafkauser.repository;

import org.example.kafkauser.adapter.out.persistence.entity.UsersOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersOutboxRepository extends JpaRepository<UsersOutboxEntity, Long> {
    Optional<UsersOutboxEntity> findByPayloadAndEventType(Long payload, String eventType);
}
