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
@Table("loss")
public class Loss {

    @Id
    private Long id;

    private LocalDate date;

    private Integer quantity;

    private String cause;

    private String description;

    @Column("sowing_id")
    private Long sowingId;

    @Column("created_at")
    private LocalDateTime createdAt;
}