package pe.agro.farmservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.agro.farmservice.dto.PlotEventDTO;
import pe.agro.farmservice.dto.PlotResponseDTO;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlotEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATED = "plot-created";
    private static final String TOPIC_UPDATED = "plot-updated";
    private static final String TOPIC_DELETED = "plot-deleted";

    public void publishCreated(PlotResponseDTO plot) {
        PlotEventDTO event = PlotEventDTO.builder()
                .eventType("CREATED")
                .plotId(plot.getId())
                .plotCode(plot.getCode())
                .plotName(plot.getName())
                .userId(plot.getUserId())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_CREATED, plot.getId().toString(), event);
        log.info("Event published: Plot created - id: {}, code: {}", plot.getId(), plot.getCode());
    }

    public void publishUpdated(PlotResponseDTO plot) {
        PlotEventDTO event = PlotEventDTO.builder()
                .eventType("UPDATED")
                .plotId(plot.getId())
                .plotCode(plot.getCode())
                .plotName(plot.getName())
                .userId(plot.getUserId())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_UPDATED, plot.getId().toString(), event);
        log.info("Event published: Plot updated - id: {}", plot.getId());
    }

    public void publishDeleted(Long plotId, String plotCode, Long userId) {
        PlotEventDTO event = PlotEventDTO.builder()
                .eventType("DELETED")
                .plotId(plotId)
                .plotCode(plotCode)
                .userId(userId)
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_DELETED, plotId.toString(), event);
        log.info("Event published: Plot deleted - id: {}", plotId);
    }
}