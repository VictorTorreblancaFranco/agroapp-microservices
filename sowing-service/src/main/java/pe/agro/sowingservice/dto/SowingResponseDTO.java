package pe.agro.sowingservice.dto;

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
public class SowingResponseDTO {
    private Long id;
    private LocalDate sowingDate;
    private LocalDate estimatedHarvestDate;
    private LocalDate actualHarvestDate;
    private Integer seedQuantity;
    private String status;
    private String observations;
    private Long cropId;
    private Long plotId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}