package org.example.kafkauser.port.in;

import org.example.kafkauser.dto.controller.user.save.response.UsersSignUpResponseDto;
import org.example.kafkauser.dto.refined.SignUpDto;

public interface AuthUseCase {
    UsersSignUpResponseDto signUp(SignUpDto signUpDto);
}
