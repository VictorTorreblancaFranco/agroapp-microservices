package pe.agro.predictionservice.dto;

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
public class PredictionResponseDTO {
    private Long id;
    private LocalDate date;
    private Double estimatedYield;
    private Double successProbability;
    private String method;
    private String observations;
    private Long sowingId;
    private LocalDateTime createdAt;
}
