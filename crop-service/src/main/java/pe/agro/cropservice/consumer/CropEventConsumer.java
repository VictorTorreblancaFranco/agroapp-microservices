package pe.agro.cropservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CropEventConsumer {

    @KafkaListener(topics = "user-created", groupId = "crop-service-group")
    public void consumeUserCreated(String message) {
        log.info("📨 CROP-SERVICE recibió evento user-created: {}", message);
        // Aquí puedes crear parcelas por defecto para el nuevo usuario
    }

    @KafkaListener(topics = "user-deleted", groupId = "crop-service-group")
    public void consumeUserDeleted(String message) {
        log.info("📨 CROP-SERVICE recibió evento user-deleted: {}", message);
        // Aquí puedes desactivar parcelas del usuario eliminado
    }
}