package com.lucas.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucas.spring.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

	PasswordResetTokenEntity findByToken(String token);

}
