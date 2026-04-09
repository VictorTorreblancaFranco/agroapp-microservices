package pe.agro.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.agro.userservice.dto.AuthRequestDTO;
import pe.agro.userservice.dto.AuthResponseDTO;
import pe.agro.userservice.model.User;
import pe.agro.userservice.repository.UserRepository;
import pe.agro.userservice.security.JwtUtil;
import pe.agro.userservice.service.AuthService;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<AuthResponseDTO> login(AuthRequestDTO request) {
        log.info("Login attempt for user: {}", request.getUsername());

        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid password"));
                    }

                    if (!user.isEnabled()) {
                        return Mono.error(new RuntimeException("Account disabled"));
                    }

                    String token = jwtUtil.generateToken(user);

                    AuthResponseDTO response = AuthResponseDTO.builder()
                            .token(token)
                            .type("Bearer")
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .roles(user.getRoles())
                            .build();

                    log.info("Login successful for user: {}", request.getUsername());
                    return Mono.just(response);
                });
    }

    @Override
    public Mono<Void> logout(Long userId) {
        log.info("Logout for user: {}", userId);
        return Mono.empty();
    }
}