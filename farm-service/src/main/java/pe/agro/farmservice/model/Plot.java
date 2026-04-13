package pe.agro.farmservice.model;

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
@Table("plot")
public class Plot {

    @Id
    private Long id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("location")
    private String location;

    @Column("area_hectares")
    private Double areaHectares;

    @Column("soil_type")
    private String soilType;

    @Column("user_id")
    private Long userId;

    @Column("registration_date")
    private LocalDate registrationDate;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("version")
    private Integer version;
}