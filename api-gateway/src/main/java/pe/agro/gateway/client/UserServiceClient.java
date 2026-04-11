package pe.agro.gateway.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.agro.gateway.dto.UserResponseDTO;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

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
                .doOnSubscribe(s -> log.info("Making HTTP request..."))
                .doOnSuccess(result -> log.info("Response received: {}", result))
                .doOnError(error -> log.error("HTTP Request failed: {}", error.getMessage(), error))
                .timeout(Duration.ofSeconds(10))
                .onErrorReturn(false);
    }

    public Mono<UserResponseDTO> getUserByUsername(String username) {
        String url = String.format("%s/api/v1/users/username/%s", userServiceUrl, username);

        log.info("=== GET USER BY USERNAME CALL ===");
        log.info("URL: {}", url);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .doOnSubscribe(s -> log.info("Making HTTP request..."))
                .doOnSuccess(result -> log.info("Response received: {}", result))
                .doOnError(error -> log.error("HTTP Request failed: {}", error.getMessage(), error))
                .timeout(Duration.ofSeconds(10));
    }
}