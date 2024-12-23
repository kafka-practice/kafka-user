package org.example.kafkauser.port.in;

import org.example.kafkauser.dto.controller.user.save.request.UsersSignUpRequestDto;
import org.example.kafkauser.dto.controller.user.save.response.UsersSignUpResponseDto;
import org.example.kafkauser.dto.processed.SignUpDto;

public interface AuthUseCase {
    UsersSignUpResponseDto signUp(SignUpDto signUpDto);
}
