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
public class WeatherResponseDTO {
    private Long id;
    private LocalDate date;
    private Double temperature;
    private Double humidity;
    private Double rainfall;
    private Double wind;
    private Long plotId;
    private LocalDateTime recordedAt;
}