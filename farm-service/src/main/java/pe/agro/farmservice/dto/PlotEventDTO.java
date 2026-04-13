package pe.agro.farmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlotEventDTO {
    private String eventType;
    private Long plotId;
    private String plotCode;
    private String plotName;
    private Long userId;
    private LocalDateTime eventTimestamp;
}