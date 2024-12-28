package org.example.kafkauser.adapter.out.persistence.adapter;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.kafkauser.adapter.out.persistence.entity.UsersEntity;
import org.example.kafkauser.common.annotation.PersistenceAdapter;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.mapper.UsersMapper;
import org.example.kafkauser.port.out.UsersCrudPort;
import org.example.kafkauser.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@Service
@RequiredArgsConstructor
public class UsersPersistenceAdapter implements UsersCrudPort {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
    public UsersDto signUp(UsersDto usersDto) {
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        return usersMapper.toDto(usersRepository.save(usersEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(UsersDto usersDto) {
        return usersRepository.existsByEmail(usersDto.getEmail());
    }

    @Transactional(readOnly = true)
    @Override
    public UsersDto findByUserId(UsersDto usersDto) {
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        UsersEntity findEntity = usersRepository.findByUserId(usersEntity.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found by userId : "+usersEntity.getUserId()));
        return usersMapper.toDto(findEntity);
    }
}
