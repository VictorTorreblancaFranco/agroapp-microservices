package pe.agro.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthDate;
    private String documentType;
    private String documentNumber;
    private String email;
    private String phone;
    private String roles;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private LocalDateTime createdAt;
}