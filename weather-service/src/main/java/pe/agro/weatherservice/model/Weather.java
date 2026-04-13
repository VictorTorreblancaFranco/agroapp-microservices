package pe.agro.weatherservice.model;

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
@Table("weather")
public class Weather {

    @Id
    private Long id;

    private LocalDate date;

    private Double temperature;

    private Double humidity;

    private Double rainfall;

    private Double wind;

    @Column("plot_id")
    private Long plotId;

    @Column("recorded_at")
    private LocalDateTime recordedAt;
}