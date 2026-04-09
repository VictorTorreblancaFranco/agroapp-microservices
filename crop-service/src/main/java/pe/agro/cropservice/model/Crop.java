package pe.agro.cropservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("crop")
public class Crop {

    @Id
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

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    private Integer version;
}