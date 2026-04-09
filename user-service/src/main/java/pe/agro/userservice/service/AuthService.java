package pe.agro.userservice.service;

import pe.agro.userservice.dto.AuthRequestDTO;
import pe.agro.userservice.dto.AuthResponseDTO;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponseDTO> login(AuthRequestDTO request);
    Mono<Void> logout(Long userId);
}