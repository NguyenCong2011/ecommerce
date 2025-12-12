package dev.cong.v.springcomereme.dao;


import dev.cong.v.springcomereme.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenAndOtp(String token, String otp);

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query(value = "DELETE FROM password_reset p WHERE p.user_id = :user_id",nativeQuery = true)
    void deleteByUserId(@Param("user_id") Long user_id);
}
