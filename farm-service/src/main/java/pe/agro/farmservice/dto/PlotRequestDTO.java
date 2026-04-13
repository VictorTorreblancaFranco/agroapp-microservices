package pe.agro.farmservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlotRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String location;

    @NotNull(message = "Area is required")
    @Positive(message = "Area must be positive")
    private Double areaHectares;

    private String soilType;

    @NotNull(message = "User ID is required")
    private Long userId;
}