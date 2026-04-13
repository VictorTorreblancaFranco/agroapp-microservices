package pe.agro.sowingservice.model;

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
@Table("sowing")
public class Sowing {

    @Id
    private Long id;

    @Column("sowing_date")
    private LocalDate sowingDate;

    @Column("estimated_harvest_date")
    private LocalDate estimatedHarvestDate;

    @Column("actual_harvest_date")
    private LocalDate actualHarvestDate;

    @Column("seed_quantity")
    private Integer seedQuantity;

    private String status;

    private String observations;

    @Column("crop_id")
    private Long cropId;

    @Column("plot_id")
    private Long plotId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    private Integer version;
}