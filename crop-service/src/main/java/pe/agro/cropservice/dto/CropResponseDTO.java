package pe.agro.cropservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropResponseDTO {
    private Long id;
    private String name;
    private String scientificName;
    private String type;
    private String variety;
    private String description;
    private String season;
    private Integer cycleDays;
    private Double expectedYield;
    private Integer sowingDensity;
    private Double optimalTempMin;
    private Double optimalTempMax;
    private Double optimalHumidityMin;
    private Double optimalHumidityMax;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}