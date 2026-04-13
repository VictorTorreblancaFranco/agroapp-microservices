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
@Table("cost")
public class Cost {

    @Id
    private Long id;

    private String description;

    private String type;

    private Double amount;

    private LocalDate date;

    @Column("payment_method")
    private String paymentMethod;

    @Column("sowing_id")
    private Long sowingId;

    @Column("created_at")
    private LocalDateTime createdAt;
}