package org.example.kafkauser.adapter.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.common.annotation.PersistenceAdapter;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.mapper.UsersMapper;
import org.example.kafkauser.port.out.UsersCrudPort;
import org.example.kafkauser.repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class UsersPersistenceAdapter implements UsersCrudPort {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Transactional
    @Override
    public UsersDto signUp(UsersDto usersDto) {
        return usersMapper.toDto(usersRepository.save(usersMapper.toEntity(usersDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(UsersDto usersDto) {
        return usersRepository.existsByEmail(usersDto.getEmail());
    }
}
