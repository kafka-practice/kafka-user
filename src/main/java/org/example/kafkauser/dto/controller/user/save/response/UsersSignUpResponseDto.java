package org.example.kafkauser.dto.controller.user.save.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UsersSignUpResponseDto {
    private Long userId;

    public static UsersSignUpResponseDto of(Long userId) {
        return UsersSignUpResponseDto.builder()
                .userId(userId)
                .build();
    }
}
