package pe.agro.userservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventConsumer {

    @KafkaListener(topics = "user-created", groupId = "user-service-group")
    public void consumeUserCreated(String message) {
        log.info("📨 EVENTO RECIBIDO (user-created): {}", message);
    }

    @KafkaListener(topics = "user-deleted", groupId = "user-service-group")
    public void consumeUserDeleted(String message) {
        log.info("📨 EVENTO RECIBIDO (user-deleted): {}", message);
    }

    @KafkaListener(topics = "user-updated", groupId = "user-service-group")
    public void consumeUserUpdated(String message) {
        log.info("📨 EVENTO RECIBIDO (user-updated): {}", message);
    }
}