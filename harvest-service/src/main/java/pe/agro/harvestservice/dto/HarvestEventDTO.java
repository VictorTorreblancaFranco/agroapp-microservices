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
public class HarvestEventDTO {
    private String eventType;
    private Long harvestId;
    private Long sowingId;
    private Double quantity;
    private String quality;
    private LocalDate harvestDate;
    private LocalDateTime eventTimestamp;
}
