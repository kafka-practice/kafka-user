package org.example.kafkauser.adapter.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.adapter.out.persistence.entity.UsersOutboxEntity;
import org.example.kafkauser.common.annotation.PersistenceAdapter;
import org.example.kafkauser.common.exception.ErrorCode;
import org.example.kafkauser.common.exception.UsersException;
import org.example.kafkauser.dto.jpa.UsersOutboxDto;
import org.example.kafkauser.mapper.UsersOutboxMapper;
import org.example.kafkauser.port.out.UsersOutboxCrudPort;
import org.example.kafkauser.repository.UsersOutboxRepository;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class UsersOutboxPersistenceAdapter implements UsersOutboxCrudPort {
    private final UsersOutboxMapper usersOutboxMapper;
    private final UsersOutboxRepository usersOutboxRepository;

    @Transactional
    @Override
    public Long saveUsersOutboxEvent(UsersOutboxDto usersOutboxDto) {
        UsersOutboxEntity usersOutboxEntity = usersOutboxMapper.toEntity(usersOutboxDto);
        UsersOutboxEntity savedEntity = usersOutboxRepository.save(usersOutboxEntity);
        return savedEntity.getOutboxId();
    }

    @Transactional(readOnly = true)
    @Override
    public UsersOutboxDto findUsersOutboxBy(Long payload, String eventType) {
        UsersOutboxEntity foundEntity = usersOutboxRepository.findByPayloadAndEventType(payload, eventType)
                .orElseThrow(() -> new UsersException(ErrorCode.NOT_FOUND_USERS_OUTBOX));
        return usersOutboxMapper.toDto(foundEntity);
    }
}
