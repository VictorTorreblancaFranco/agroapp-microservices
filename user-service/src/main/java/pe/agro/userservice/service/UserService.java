package pe.agro.userservice.service;

import pe.agro.userservice.dto.UserRequestDTO;
import pe.agro.userservice.dto.UserResponseDTO;
import pe.agro.userservice.dto.UserUpdateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponseDTO> save(UserRequestDTO request);
    Mono<UserResponseDTO> update(UserUpdateDTO request);
    Mono<Void> deleteById(Long id);
    Mono<UserResponseDTO> restore(Long id);
    Mono<UserResponseDTO> findById(Long id);
    Mono<UserResponseDTO> findByUsername(String username);
    Flux<UserResponseDTO> findAll();
    Flux<UserResponseDTO> findAllActive();
    Flux<UserResponseDTO> findAllInactive();
    Flux<UserResponseDTO> findAllActiveAndVerified();
    Mono<UserResponseDTO> findByEmail(String email);
    Mono<Void> incrementFailedAttempts(Long id);
    Mono<Void> resetFailedAttempts(Long id);
    Mono<Boolean> verifyEmail(String token);
    Mono<Void> changePassword(Long id, String newPassword);
    Mono<Void> requestPasswordRecovery(String email);
    Mono<Boolean> isEmailAvailable(String email);
    Mono<Boolean> isPhoneAvailable(String phone);
    Mono<UserResponseDTO> changeRole(Long userId, String role);
}