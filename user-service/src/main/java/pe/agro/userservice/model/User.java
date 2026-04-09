package pe.agro.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("document_type")
    private String documentType;

    @Column("document_number")
    private String documentNumber;

    private String email;
    private String phone;
    private String password;
    private String username;
    private String roles;

    @Column("failed_attempts")
    private Integer failedAttempts;

    @Column("locked_until")
    private LocalDateTime lockedUntil;

    @Column("last_access")
    private LocalDateTime lastAccess;

    @Column("last_password_change")
    private LocalDateTime lastPasswordChange;

    private Integer version;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("is_active")
    private Boolean isActive;

    @Column("deleted_at")
    private LocalDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("is_email_verified")
    private Boolean isEmailVerified;

    @Column("verification_token")
    private String verificationToken;

    @Column("recovery_token")
    private String recoveryToken;

    @Column("token_expiry")
    private LocalDateTime tokenExpiry;

    @Column("secret_question")
    private String secretQuestion;

    @Column("secret_answer")
    private String secretAnswer;

    @Column("registration_ip")
    private String registrationIp;

    @Column("account_expiry_date")
    private LocalDate accountExpiryDate;

    @Column("account_non_locked")
    private Boolean accountNonLocked;

    @Column("account_non_expired")
    private Boolean accountNonExpired;

    @Column("credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column("last_password_reset_date")
    private LocalDateTime lastPasswordResetDate;
}