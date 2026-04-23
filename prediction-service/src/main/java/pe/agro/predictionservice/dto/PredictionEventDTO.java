package pe.agro.predictionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionEventDTO {
    private String eventType;
    private Long predictionId;
    private Long sowingId;
    private Double estimatedYield;
    private Double successProbability;
    private LocalDateTime eventTimestamp;
}
