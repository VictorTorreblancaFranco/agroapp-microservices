package pe.agro.harvestservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.agro.harvestservice.dto.HarvestEventDTO;
import pe.agro.harvestservice.dto.HarvestResponseDTO;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class HarvestEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATED = "harvest-created";
    private static final String TOPIC_UPDATED = "harvest-updated";

    public void publishCreated(HarvestResponseDTO harvest) {
        HarvestEventDTO event = HarvestEventDTO.builder()
                .eventType("CREATED")
                .harvestId(harvest.getId())
                .sowingId(harvest.getSowingId())
                .quantity(harvest.getQuantity())
                .quality(harvest.getQuality())
                .harvestDate(harvest.getHarvestDate())
                .eventTimestamp(LocalDateTime.now())
                .build();
        
        kafkaTemplate.send(TOPIC_CREATED, harvest.getId().toString(), event);
        log.info("Event published: Harvest created - id: {}", harvest.getId());
    }

    public void publishUpdated(HarvestResponseDTO harvest) {
        HarvestEventDTO event = HarvestEventDTO.builder()
                .eventType("UPDATED")
                .harvestId(harvest.getId())
                .sowingId(harvest.getSowingId())
                .quantity(harvest.getQuantity())
                .quality(harvest.getQuality())
                .harvestDate(harvest.getHarvestDate())
                .eventTimestamp(LocalDateTime.now())
                .build();
        
        kafkaTemplate.send(TOPIC_UPDATED, harvest.getId().toString(), event);
        log.info("Event published: Harvest updated - id: {}", harvest.getId());
    }
}
