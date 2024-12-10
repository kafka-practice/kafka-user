package org.example.kafkauser.controller;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.dto.controller.ApiResponse;
import org.example.kafkauser.dto.controller.user.save.request.UsersSignUpRequestDto;
import org.example.kafkauser.dto.controller.user.save.response.UsersSignUpResponseDto;
import org.example.kafkauser.dto.refined.SignUpDto;
import org.example.kafkauser.port.in.AuthUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {
    private final AuthUseCase authUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UsersSignUpResponseDto>> signup(
            @RequestBody UsersSignUpRequestDto usersSignUpRequestDto
    ) {
        SignUpDto signUpDto = SignUpDto.of(usersSignUpRequestDto);
        UsersSignUpResponseDto usersSignUpResponseDto = authUseCase.signUp(signUpDto);
        return ResponseEntity.ok(ApiResponse.success(usersSignUpResponseDto));
    }
}
