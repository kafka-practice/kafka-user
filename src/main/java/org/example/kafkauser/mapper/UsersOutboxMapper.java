package org.example.kafkauser.mapper;

import org.example.kafkauser.common.mapper.GenericMapper;
import org.example.kafkauser.adapter.out.persistence.entity.UsersOutboxEntity;
import org.example.kafkauser.dto.jpa.UsersOutboxDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersOutboxMapper extends GenericMapper<UsersOutboxDto, UsersOutboxEntity> {

}
