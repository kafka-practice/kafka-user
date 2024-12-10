package org.example.kafkauser.repository;

import org.example.kafkauser.adapter.out.persistence.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByEmail (String email);
}
