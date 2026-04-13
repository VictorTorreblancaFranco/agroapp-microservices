package pe.agro.weatherservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRequestDTO {

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Double temperature;

    private Double humidity;

    private Double rainfall;

    private Double wind;

    @NotNull(message = "Plot ID is required")
    private Long plotId;
}