package pe.agro.userservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.userservice.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findByEmail(String email);
    Mono<User> findByUsername(String username);
    Mono<User> findByPhone(String phone);
    Mono<User> findByVerificationToken(String token);
    Mono<User> findByRecoveryToken(String token);

    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByPhone(String phone);
    Mono<Boolean> existsByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);

    @Query("UPDATE users SET is_active = false, deleted_at = CURRENT_TIMESTAMP, deleted_by = :deletedBy WHERE id = :id")
    Mono<Integer> softDeleteById(@Param("id") Long id, @Param("deletedBy") String deletedBy);

    @Query("UPDATE users SET is_active = true, deleted_at = NULL, deleted_by = NULL WHERE id = :id")
    Mono<Integer> restoreById(@Param("id") Long id);

    @Query("UPDATE users SET failed_attempts = failed_attempts + 1, locked_until = CASE WHEN failed_attempts + 1 >= 5 THEN :lockedUntil ELSE NULL END WHERE id = :id")
    Mono<Integer> incrementFailedAttempts(@Param("id") Long id, @Param("lockedUntil") LocalDateTime lockedUntil);

    @Query("UPDATE users SET failed_attempts = 0, locked_until = NULL, last_access = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> resetFailedAttempts(@Param("id") Long id);

    @Query("UPDATE users SET is_email_verified = true WHERE id = :id")
    Mono<Integer> verifyEmail(@Param("id") Long id);

    @Query("UPDATE users SET password = :newPassword, last_password_change = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> changePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    Flux<User> findByIsActiveTrue();
    Flux<User> findByIsActiveFalse();

    @Query("SELECT * FROM users WHERE is_active = true AND is_email_verified = true")
    Flux<User> findAllActiveAndVerified();
}