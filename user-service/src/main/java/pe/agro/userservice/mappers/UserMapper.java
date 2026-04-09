package pe.agro.userservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.userservice.dto.UserRequestDTO;
import pe.agro.userservice.dto.UserResponseDTO;
import pe.agro.userservice.dto.UserUpdateDTO;
import pe.agro.userservice.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO request, String username, String plainPassword) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(username)
                .birthDate(request.getBirthDate())
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .email(request.getEmail())
                .password(plainPassword)
                .phone(request.getPhone())
                .roles("ROLE_USER")
                .isActive(true)
                .isEmailVerified(false)
                .failedAttempts(0)
                .version(1)
                .createdAt(LocalDateTime.now())
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiry(LocalDateTime.now().plusDays(1))
                .build();
    }

    public User toEntity(UserUpdateDTO request, User existing) {
        if (request == null || existing == null) {
            return existing;
        }

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setBirthDate(request.getBirthDate());
        existing.setDocumentType(request.getDocumentType());
        existing.setDocumentNumber(request.getDocumentNumber());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existing.setPassword(request.getPassword());
        }

        return existing;
    }

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .birthDate(user.getBirthDate())
                .documentType(user.getDocumentType())
                .documentNumber(user.getDocumentNumber())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}