package pe.agro.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishUserCreated(Long userId, String email, String username) {
        String message = String.format("{\"event\":\"USER_CREATED\",\"userId\":%d,\"email\":\"%s\",\"username\":\"%s\"}",
                userId, email, username);
        kafkaTemplate.send("user-created", userId.toString(), message);
        log.info("📢 Evento publicado en 'user-created': {}", message);
    }

    public void publishUserDeleted(Long userId) {
        String message = String.format("{\"event\":\"USER_DELETED\",\"userId\":%d}", userId);
        kafkaTemplate.send("user-deleted", userId.toString(), message);
        log.info("📢 Evento publicado en 'user-deleted': {}", message);
    }

    public void publishUserUpdated(Long userId, String email) {
        String message = String.format("{\"event\":\"USER_UPDATED\",\"userId\":%d,\"email\":\"%s\"}", userId, email);
        kafkaTemplate.send("user-updated", userId.toString(), message);
        log.info("📢 Evento publicado en 'user-updated': {}", message);
    }
}