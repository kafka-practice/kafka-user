package org.example.kafkauser.dto.refined;

import lombok.*;
import org.example.kafkauser.dto.controller.user.save.request.UsersSignUpRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 생성자의 접근 제어자를 private으로 설정
public class SignUpDto {
    private String name;
    private String email;
    private String password;
    private String nickname;

    public static SignUpDto of(UsersSignUpRequestDto usersSignUpRequestDto) {
        return SignUpDto.builder()
                .name(usersSignUpRequestDto.getName())
                .email(usersSignUpRequestDto.getEmail())
                .password(usersSignUpRequestDto.getPassword())
                .nickname(usersSignUpRequestDto.getNickname())
                .build();
    }
}
