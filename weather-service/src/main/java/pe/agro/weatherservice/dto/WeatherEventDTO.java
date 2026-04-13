package pe.agro.weatherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherEventDTO {
    private String eventType;
    private Long weatherId;
    private Long plotId;
    private LocalDate date;
    private Double temperature;
    private Double humidity;
    private Double rainfall;
    private LocalDateTime eventTimestamp;
}