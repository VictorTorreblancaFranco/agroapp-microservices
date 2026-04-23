package pe.agro.predictionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.agro.predictionservice.dto.PredictionEventDTO;
import pe.agro.predictionservice.dto.PredictionResponseDTO;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictionEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_CREATED = "prediction-created";
    private static final String TOPIC_UPDATED = "prediction-updated";

    public void publishCreated(PredictionResponseDTO prediction) {
        PredictionEventDTO event = PredictionEventDTO.builder()
                .eventType("CREATED")
                .predictionId(prediction.getId())
                .sowingId(prediction.getSowingId())
                .estimatedYield(prediction.getEstimatedYield())
                .successProbability(prediction.getSuccessProbability())
                .eventTimestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send(TOPIC_CREATED, prediction.getId().toString(), event);
        log.info("Event published: Prediction created - id: {}", prediction.getId());
    }

    public void publishUpdated(PredictionResponseDTO prediction) {
        PredictionEventDTO event = PredictionEventDTO.builder()
                .eventType("UPDATED")
                .predictionId(prediction.getId())
                .sowingId(prediction.getSowingId())
                .estimatedYield(prediction.getEstimatedYield())
                .successProbability(prediction.getSuccessProbability())
                .eventTimestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send(TOPIC_UPDATED, prediction.getId().toString(), event);
        log.info("Event published: Prediction updated - id: {}", prediction.getId());
    }
}
