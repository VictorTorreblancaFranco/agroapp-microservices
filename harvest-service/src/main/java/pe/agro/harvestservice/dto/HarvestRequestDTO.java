package pe.agro.harvestservice.dto;

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
public class HarvestRequestDTO {
    
    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;
    
    private String quality;
    
    private String observations;
    
    @NotNull(message = "Sowing ID is required")
    private Long sowingId;
}
