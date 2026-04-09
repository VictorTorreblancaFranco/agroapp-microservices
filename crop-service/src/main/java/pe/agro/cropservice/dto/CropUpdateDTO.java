package pe.agro.cropservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropUpdateDTO {

    @NotNull(message = "El ID es obligatorio")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Size(max = 150, message = "El nombre científico no puede exceder 150 caracteres")
    private String scientificName;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "CEREAL|LEGUMINOSA|TUBERCULO|HORTALIZA|FRUTAL|OTRO",
            message = "Tipo debe ser: CEREAL, LEGUMINOSA, TUBERCULO, HORTALIZA, FRUTAL, OTRO")
    private String type;

    @Size(max = 100, message = "La variedad no puede exceder 100 caracteres")
    private String variety;

    private String description;

    @Pattern(regexp = "VERANO|INVIERNO|PRIMAVERA|OTOÑO|TODO_EL_AÑO",
            message = "Temporada debe ser: VERANO, INVIERNO, PRIMAVERA, OTOÑO, TODO_EL_AÑO")
    private String season;

    @NotNull(message = "Los días de ciclo son obligatorios")
    @Positive(message = "Los días de ciclo deben ser mayores a 0")
    private Integer cycleDays;

    @NotNull(message = "El rendimiento esperado es obligatorio")
    @PositiveOrZero(message = "El rendimiento esperado debe ser mayor o igual a 0")
    private Double expectedYield;

    @Positive(message = "La densidad de siembra debe ser mayor a 0")
    private Integer sowingDensity;

    @DecimalMin(value = "-20.0", message = "Temperatura mínima no puede ser menor a -20°C")
    @DecimalMax(value = "60.0", message = "Temperatura mínima no puede ser mayor a 60°C")
    private Double optimalTempMin;

    @DecimalMin(value = "-20.0", message = "Temperatura máxima no puede ser menor a -20°C")
    @DecimalMax(value = "60.0", message = "Temperatura máxima no puede ser mayor a 60°C")
    private Double optimalTempMax;

    @Min(value = 0, message = "Humedad mínima no puede ser menor a 0%")
    @Max(value = 100, message = "Humedad mínima no puede ser mayor a 100%")
    private Double optimalHumidityMin;

    @Min(value = 0, message = "Humedad máxima no puede ser menor a 0%")
    @Max(value = 100, message = "Humedad máxima no puede ser mayor a 100%")
    private Double optimalHumidityMax;

    private Boolean isActive;
}