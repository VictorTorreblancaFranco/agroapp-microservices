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
public class LossResponseDTO {
    private Long id;
    private LocalDate date;
    private Integer quantity;
    private String cause;
    private String description;
    private Long sowingId;
    private LocalDateTime createdAt;
}