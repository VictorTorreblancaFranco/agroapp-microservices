package pe.agro.predictionservice.model;

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
@Table("prediction")
public class Prediction {
    @Id
    private Long id;
    private LocalDate date;
    @Column("estimated_yield")
    private Double estimatedYield;
    @Column("success_probability")
    private Double successProbability;
    private String method;
    private String observations;
    @Column("sowing_id")
    private Long sowingId;
    @Column("created_at")
    private LocalDateTime createdAt;
}
