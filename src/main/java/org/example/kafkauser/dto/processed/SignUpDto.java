package org.example.kafkauser.dto.processed;

import lombok.*;
import org.example.kafkauser.dto.controller.user.save.request.UsersSignUpRequestDto;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
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
