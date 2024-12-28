package org.example.kafkauser.port.in;

import org.example.kafkauser.dto.jpa.UsersDto;

public interface UsersUseCase {
    UsersDto findByUserId(UsersDto usersDto);
}
