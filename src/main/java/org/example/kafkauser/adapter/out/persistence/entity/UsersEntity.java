package org.example.kafkauser.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.kafkauser.common.entity.BaseEntity;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Where(clause = "use_yn = true")
public class UsersEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
