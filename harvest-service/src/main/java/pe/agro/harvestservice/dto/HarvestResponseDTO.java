package pe.agro.harvestservice.dto;

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
public class HarvestResponseDTO {
    private Long id;
    private LocalDate harvestDate;
    private Double quantity;
    private String quality;
    private String observations;
    private Long sowingId;
    private LocalDateTime createdAt;
}
