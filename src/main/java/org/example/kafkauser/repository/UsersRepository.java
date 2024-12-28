package org.example.kafkauser.repository;

import org.example.kafkauser.adapter.out.persistence.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByEmail (String email);

    @Query("SELECT u FROM UsersEntity u WHERE u.userId = :userId")
    Optional<UsersEntity> findByUserId (@Param("userId") Long userId);
}
