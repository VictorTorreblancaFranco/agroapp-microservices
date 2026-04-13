package pe.agro.sowingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SowingEventDTO {
    private String eventType;
    private Long sowingId;
    private Long cropId;
    private Long plotId;
    private Integer seedQuantity;
    private String status;
    private LocalDateTime eventTimestamp;
}