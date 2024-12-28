package org.example.kafkauser.service.user;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.common.annotation.UseCase;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.port.in.UsersUseCase;
import org.example.kafkauser.port.out.UsersCrudPort;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UsersFindService implements UsersUseCase {
    private final UsersCrudPort usersCrudPort;

    @Override
    public UsersDto findByUserId(UsersDto usersDto) {
        return usersCrudPort.findByUserId(usersDto);
    }
}
