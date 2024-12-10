package org.example.kafkauser.port.out;

import org.example.kafkauser.dto.jpa.UsersOutboxDto;

public interface UsersOutboxCrudPort {
    Long saveUsersOutboxEvent(UsersOutboxDto usersOutboxDto);
    UsersOutboxDto findUsersOutboxBy(Long payload, String eventType);
}
