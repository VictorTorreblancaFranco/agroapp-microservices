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
public class CostResponseDTO {
    private Long id;
    private String description;
    private String type;
    private Double amount;
    private LocalDate date;
    private String paymentMethod;
    private Long sowingId;
    private LocalDateTime createdAt;
}