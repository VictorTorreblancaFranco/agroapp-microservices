package pe.agro.predictionservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestDTO {
    @NotNull(message = "Date is required")
    private LocalDate date;
    @NotNull(message = "Estimated yield is required")
    @Positive(message = "Estimated yield must be positive")
    private Double estimatedYield;
    @NotNull(message = "Success probability is required")
    @Min(0) @Max(100)
    private Double successProbability;
    private String method;
    private String observations;
    @NotNull(message = "Sowing ID is required")
    private Long sowingId;
}
