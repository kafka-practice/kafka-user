package org.example.kafkauser.mapper;

import org.example.kafkauser.common.mapper.GenericMapper;
import org.example.kafkauser.dto.controller.user.save.response.UsersSignUpResponseDto;
import org.example.kafkauser.dto.jpa.UsersDto;
import org.example.kafkauser.dto.refined.SignUpDto;
import org.example.kafkauser.adapter.out.persistence.entity.UsersEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper extends GenericMapper<UsersDto, UsersEntity> {
    UsersDto processedToDto(SignUpDto signUpDto);
    UsersSignUpResponseDto dtoToResponse(UsersDto usersDto);
}
