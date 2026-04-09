package pe.agro.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.agro.userservice.dto.UserRequestDTO;
import pe.agro.userservice.dto.UserResponseDTO;
import pe.agro.userservice.dto.UserUpdateDTO;
import pe.agro.userservice.mapper.UserMapper;
import pe.agro.userservice.repository.UserRepository;
import pe.agro.userservice.service.EventPublisherService;
import pe.agro.userservice.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EventPublisherService eventPublisherService;

    @Override
    public Mono<UserResponseDTO> save(UserRequestDTO request) {
        log.info("Saving new user: {}", request.getEmail());

        return validateUniqueConstraints(request)
                .then(generateUsername(request.getFirstName(), request.getLastName()))
                .flatMap(username -> {
                    String defaultPassword = "agroapp123";
                    String encodedPassword = passwordEncoder.encode(defaultPassword);
                    var user = userMapper.toEntity(request, username, encodedPassword);
                    return userRepository.save(user);
                })
                .map(userMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("User saved successfully with id: {}, username: {}, password: agroapp123",
                            response.getId(), response.getUsername());
                    eventPublisherService.publishUserCreated(response.getId(), response.getEmail(), response.getUsername());
                })
                .doOnError(error -> log.error("Error saving user: {}", error.getMessage()));
    }

    private Mono<String> generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName.substring(0, 1) + lastName).toLowerCase()
                .replaceAll("[^a-zA-Z0-9]", "");

        return userRepository.existsByUsername(baseUsername)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(baseUsername);
                    }
                    return findAvailableUsername(baseUsername, 1);
                });
    }

    private Mono<String> findAvailableUsername(String baseUsername, int counter) {
        String newUsername = baseUsername + counter;
        return userRepository.existsByUsername(newUsername)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.just(newUsername);
                    }
                    return findAvailableUsername(baseUsername, counter + 1);
                });
    }

    @Override
    public Mono<UserResponseDTO> update(UserUpdateDTO request) {
        log.info("Updating user with id: {}", request.getId());

        return userRepository.findById(request.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + request.getId())))
                .map(existing -> {
                    String encodedPassword = null;
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        encodedPassword = passwordEncoder.encode(request.getPassword());
                    }
                    return userMapper.toEntity(request, existing, encodedPassword);
                })
                .flatMap(userRepository::save)
                .map(userMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("User updated successfully: {}", response.getId());
                    eventPublisherService.publishUserUpdated(response.getId(), response.getEmail());
                })
                .doOnError(error -> log.error("Error updating user: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Soft deleting user with id: {}", id);
        return userRepository.softDeleteById(id, "system")
                .doOnSuccess(rows -> {
                    if (rows > 0) {
                        eventPublisherService.publishUserDeleted(id);
                    }
                })
                .then();
    }

    @Override
    public Mono<UserResponseDTO> restore(Long id) {
        log.info("Restoring user with id: {}", id);
        return userRepository.restoreById(id)
                .then(userRepository.findById(id))
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Mono<UserResponseDTO> findById(Long id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)))
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Mono<UserResponseDTO> findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with username: " + username)))
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Flux<UserResponseDTO> findAll() {
        log.info("Finding all users");
        return userRepository.findAll()
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Flux<UserResponseDTO> findAllActive() {
        log.info("Finding all active users");
        return userRepository.findByIsActiveTrue()
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Flux<UserResponseDTO> findAllInactive() {
        log.info("Finding all inactive users");
        return userRepository.findByIsActiveFalse()
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Flux<UserResponseDTO> findAllActiveAndVerified() {
        log.info("Finding all active and verified users");
        return userRepository.findAllActiveAndVerified()
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Mono<UserResponseDTO> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with email: " + email)))
                .map(userMapper::toResponseDTO);
    }

    @Override
    public Mono<Void> incrementFailedAttempts(Long id) {
        log.info("Incrementing failed attempts for user: {}", id);
        LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(30);
        return userRepository.incrementFailedAttempts(id, lockedUntil).then();
    }

    @Override
    public Mono<Void> resetFailedAttempts(Long id) {
        log.info("Resetting failed attempts for user: {}", id);
        return userRepository.resetFailedAttempts(id).then();
    }

    @Override
    public Mono<Boolean> verifyEmail(String token) {
        log.info("Verifying email with token: {}", token);
        return userRepository.findByVerificationToken(token)
                .flatMap(user -> {
                    if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(LocalDateTime.now())) {
                        return Mono.error(new RuntimeException("Token expired"));
                    }
                    return userRepository.verifyEmail(user.getId()).map(rows -> true);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid token")));
    }

    @Override
    public Mono<Void> changePassword(Long id, String newPassword) {
        log.info("Changing password for user: {}", id);
        return userRepository.changePassword(id, passwordEncoder.encode(newPassword)).then();
    }

    @Override
    public Mono<Void> requestPasswordRecovery(String email) {
        log.info("Requesting password recovery for: {}", email);
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Email not registered")))
                .flatMap(user -> {
                    String token = UUID.randomUUID().toString();
                    user.setRecoveryToken(token);
                    user.setTokenExpiry(LocalDateTime.now().plusHours(1));
                    return userRepository.save(user);
                })
                .then();
    }

    @Override
    public Mono<Boolean> isEmailAvailable(String email) {
        return userRepository.existsByEmail(email).map(exists -> !exists);
    }

    @Override
    public Mono<Boolean> isPhoneAvailable(String phone) {
        return userRepository.existsByPhone(phone).map(exists -> !exists);
    }

    @Override
    public Mono<UserResponseDTO> changeRole(Long userId, String role) {
        log.info("Changing role for user id: {} to: {}", userId, role);

        String validRole = "ROLE_" + role.toUpperCase();
        if (!validRole.equals("ROLE_USER") && !validRole.equals("ROLE_MANAGER") &&
                !validRole.equals("ROLE_ADMIN") && !validRole.equals("ROLE_SUPER_ADMIN")) {
            return Mono.error(new RuntimeException("Invalid role. Allowed roles: USER, MANAGER, ADMIN, SUPER_ADMIN"));
        }

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)))
                .flatMap(user -> {
                    user.setRoles(validRole);
                    return userRepository.save(user);
                })
                .map(userMapper::toResponseDTO);
    }

    private Mono<Void> validateUniqueConstraints(UserRequestDTO request) {
        return userRepository.existsByEmail(request.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new RuntimeException("Email already registered: " + request.getEmail()));
                    }
                    return userRepository.existsByDocumentTypeAndDocumentNumber(request.getDocumentType(), request.getDocumentNumber());
                })
                .flatMap(docExists -> {
                    if (docExists) {
                        return Mono.error(new RuntimeException("Document already registered: " + request.getDocumentType() + " - " + request.getDocumentNumber()));
                    }
                    return userRepository.existsByPhone(request.getPhone());
                })
                .flatMap(phoneExists -> {
                    if (phoneExists) {
                        return Mono.error(new RuntimeException("Phone already registered: " + request.getPhone()));
                    }
                    return Mono.empty();
                });
    }
}