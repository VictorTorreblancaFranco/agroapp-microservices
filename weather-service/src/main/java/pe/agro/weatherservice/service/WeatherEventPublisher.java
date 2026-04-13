package pe.agro.weatherservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.agro.weatherservice.dto.WeatherEventDTO;
import pe.agro.weatherservice.dto.WeatherResponseDTO;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATED = "weather-created";
    private static final String TOPIC_UPDATED = "weather-updated";

    public void publishCreated(WeatherResponseDTO weather) {
        WeatherEventDTO event = WeatherEventDTO.builder()
                .eventType("CREATED")
                .weatherId(weather.getId())
                .plotId(weather.getPlotId())
                .date(weather.getDate())
                .temperature(weather.getTemperature())
                .humidity(weather.getHumidity())
                .rainfall(weather.getRainfall())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_CREATED, weather.getId().toString(), event);
        log.info("Event published: Weather created - id: {}", weather.getId());
    }

    public void publishUpdated(WeatherResponseDTO weather) {
        WeatherEventDTO event = WeatherEventDTO.builder()
                .eventType("UPDATED")
                .weatherId(weather.getId())
                .plotId(weather.getPlotId())
                .date(weather.getDate())
                .temperature(weather.getTemperature())
                .humidity(weather.getHumidity())
                .rainfall(weather.getRainfall())
                .eventTimestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC_UPDATED, weather.getId().toString(), event);
        log.info("Event published: Weather updated - id: {}", weather.getId());
    }
}