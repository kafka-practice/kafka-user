package org.example.kafkauser.dto.jpa;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UsersDto {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private boolean useYn;


    public static UsersDto of(long id) {
        return UsersDto.builder()
                .userId(id)
                .build();
    };
}
