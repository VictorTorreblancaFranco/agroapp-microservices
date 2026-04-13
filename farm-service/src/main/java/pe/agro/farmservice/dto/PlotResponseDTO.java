package pe.agro.farmservice.dto;

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
public class PlotResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String location;
    private Double areaHectares;
    private String soilType;
    private Long userId;
    private LocalDate registrationDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}