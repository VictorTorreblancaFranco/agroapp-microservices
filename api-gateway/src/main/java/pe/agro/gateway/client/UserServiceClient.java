package pe.agro.gateway.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.agro.gateway.dto.UserResponseDTO;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    /**
     * Valida las credenciales del usuario contra user-service
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Mono<Boolean> true si las credenciales son válidas, false en caso contrario
     */
    public Mono<Boolean> validateCredentials(String username, String password) {
        String url = String.format("%s/api/v1/users/validate?username=%s&password=%s",
                userServiceUrl, username, password);

        log.info("=== VALIDATE CREDENTIALS CALL ===");
        log.info("URL: {}", url);
        log.info("Username: {}", username);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(3))
                        .doBeforeRetry(retry -> log.warn("Retrying validate credentials, attempt: {}", retry.totalRetries() + 1)))
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("HTTP Error calling user-service: Status {}, Body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.just(false);
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error calling user-service validate endpoint: {}", e.getMessage());
                    return Mono.just(false);
                })
                .doOnSubscribe(s -> log.debug("Making HTTP request to validate credentials..."))
                .doOnSuccess(result -> log.info("Validate credentials response: {}", result))
                .doOnError(error -> log.error("Validate credentials failed: {}", error.getMessage()));
    }

    /**
     * Obtiene los datos completos de un usuario por su username
     * @param username Nombre de usuario
     * @return Mono<UserResponseDTO> Datos del usuario
     */
    public Mono<UserResponseDTO> getUserByUsername(String username) {
        String url = String.format("%s/api/v1/users/username/%s", userServiceUrl, username);

        log.info("=== GET USER BY USERNAME CALL ===");
        log.info("URL: {}", url);
        log.info("Username: {}", username);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(3))
                        .doBeforeRetry(retry -> log.warn("Retrying get user, attempt: {}", retry.totalRetries() + 1)))
                .onErrorResume(WebClientResponseException.NotFound.class, e -> {
                    log.error("User not found: {}", username);
                    return Mono.error(new RuntimeException("Usuario no encontrado: " + username));
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("HTTP Error calling user-service: Status {}, Body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Error comunicándose con user-service: " + e.getStatusCode()));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error calling user-service get user endpoint: {}", e.getMessage());
                    return Mono.error(new RuntimeException("Error al obtener datos del usuario: " + e.getMessage()));
                })
                .doOnSubscribe(s -> log.debug("Making HTTP request to get user by username..."))
                .doOnSuccess(result -> log.info("Get user response: id={}, username={}, roles={}",
                        result != null ? result.getId() : null,
                        result != null ? result.getUsername() : null,
                        result != null ? result.getRoles() : null))
                .doOnError(error -> log.error("Get user by username failed: {}", error.getMessage()));
    }

    /**
     * Verifica si el servicio está disponible (health check)
     * @return Mono<Boolean> true si está disponible
     */
    public Mono<Boolean> healthCheck() {
        String url = String.format("%s/actuator/health", userServiceUrl);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(2))
                .map(response -> true)
                .onErrorReturn(false)
                .doOnSuccess(available -> log.info("User-service health check: {}", available ? "UP" : "DOWN"));
    }
}