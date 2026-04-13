package pe.agro.sowingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SowingRequestDTO {

    @NotNull(message = "Sowing date is required")
    private LocalDate sowingDate;

    private LocalDate estimatedHarvestDate;

    @NotNull(message = "Seed quantity is required")
    @Positive(message = "Seed quantity must be positive")
    private Integer seedQuantity;

    private String observations;

    @NotNull(message = "Crop ID is required")
    private Long cropId;

    @NotNull(message = "Plot ID is required")
    private Long plotId;
}