package pe.agro.harvestservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("harvest")
public class Harvest {
    
    @Id
    private Long id;
    
    @Column("harvest_date")
    private LocalDate harvestDate;
    
    private Double quantity;
    
    private String quality;
    
    private String observations;
    
    @Column("sowing_id")
    private Long sowingId;
    
    @Column("created_at")
    private LocalDateTime createdAt;
}
