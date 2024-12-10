package org.example.kafkauser.service.user;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.common.annotation.UseCase;
import org.example.kafkauser.common.exception.ErrorCode;
import org.example.kafkauser.common.exception.UsersException;
import org.example.kafkauser.dto.controller.user.save.response.UsersSignUpResponseDto;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.dto.refined.SignUpDto;
import org.example.kafkauser.event.UsersCreateEvent;
import org.example.kafkauser.mapper.UsersMapper;
import org.example.kafkauser.port.in.AuthUseCase;
import org.example.kafkauser.port.out.UsersCrudPort;
import org.springframework.context.ApplicationEventPublisher;

@UseCase
@RequiredArgsConstructor
public class UsersSignUpService implements AuthUseCase {
    private final UsersCrudPort usersCrudPort;
    private final UsersMapper usersMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UsersSignUpResponseDto signUp(SignUpDto signUpDto) {
        UsersDto usersDto = usersMapper.processedToDto(signUpDto); // 0. DTO로 변경
        validateUserExist(usersDto); // 1. 이미 가입된 사용자인지 확인
        UsersDto savedDto = usersCrudPort.signUp(usersDto); // 2. 회원 가입

        eventPublisher.publishEvent(new UsersCreateEvent(savedDto.getUserId())); // 3. 이벤트 발생
        return usersMapper.dtoToResponse(savedDto); // 4. 응답값 보내기
    }

    private void validateUserExist(UsersDto usersDto) {
        boolean exist = usersCrudPort.existsByEmail(usersDto);
        if (exist) {
            throw new UsersException(ErrorCode.USERS_ALREADY_EXIST);
        }
    }
}
