package org.example.kafkauser.port.out;

import org.example.kafkauser.dto.jpa.UsersDto;

public interface UsersCrudPort {
    UsersDto signUp(UsersDto usersDto);
    boolean existsByEmail(UsersDto usersDto);
}
