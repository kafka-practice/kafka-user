package org.example.kafkauser.dto.jpa;

import lombok.*;
import org.example.kafkauser.dto.processed.SignUpDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String nickname;

    public static UsersDto of(long userId) {
        return UsersDto.builder()
                .userId(userId)
                .build();
    }
 }
