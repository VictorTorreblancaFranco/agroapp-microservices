package pe.agro.sowingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.agro.sowingservice.dto.SowingEventDTO;
import pe.agro.sowingservice.dto.SowingResponseDTO;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SowingEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATED = "sowing-created";
    private static final String TOPIC_UPDATED = "sowing-updated";
    private static final String TOPIC_STATUS_CHANGED = "sowing-status-changed";
    private static final String TOPIC_COMPLETED = "sowing-completed";

    public void publishCreated(SowingResponseDTO sowing) {
        SowingEventDTO event = SowingEventDTO.builder()
                .eventType("CREATED")
                .sowingId(sowing.getId())
                .cropId(sowing.getCropId())
                .plotId(sowing.getPlotId())
                .seedQuantity(sowing.getSeedQuantity())
                .status(sowing.getStatus())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_CREATED, sowing.getId().toString(), event);
        log.info("Event published: Sowing created - id: {}", sowing.getId());
    }

    public void publishUpdated(SowingResponseDTO sowing) {
        SowingEventDTO event = SowingEventDTO.builder()
                .eventType("UPDATED")
                .sowingId(sowing.getId())
                .cropId(sowing.getCropId())
                .plotId(sowing.getPlotId())
                .seedQuantity(sowing.getSeedQuantity())
                .status(sowing.getStatus())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_UPDATED, sowing.getId().toString(), event);
        log.info("Event published: Sowing updated - id: {}", sowing.getId());
    }

    public void publishStatusChanged(SowingResponseDTO sowing, String newStatus) {
        SowingEventDTO event = SowingEventDTO.builder()
                .eventType("STATUS_CHANGED")
                .sowingId(sowing.getId())
                .status(newStatus)
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_STATUS_CHANGED, sowing.getId().toString(), event);
        log.info("Event published: Sowing status changed - id: {}, status: {}", sowing.getId(), newStatus);
    }

    public void publishCompleted(SowingResponseDTO sowing) {
        SowingEventDTO event = SowingEventDTO.builder()
                .eventType("COMPLETED")
                .sowingId(sowing.getId())
                .cropId(sowing.getCropId())
                .plotId(sowing.getPlotId())
                .seedQuantity(sowing.getSeedQuantity())
                .status("COMPLETED")
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_COMPLETED, sowing.getId().toString(), event);
        log.info("Event published: Sowing completed - id: {}", sowing.getId());
    }
}