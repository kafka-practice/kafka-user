package org.example.kafkauser.dto.controller.user.save.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
}
