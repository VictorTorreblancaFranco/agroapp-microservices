package pe.agro.farmservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.farmservice.dto.PlotRequestDTO;
import pe.agro.farmservice.dto.PlotResponseDTO;
import pe.agro.farmservice.model.Plot;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PlotMapper {

    public Plot toEntity(PlotRequestDTO request) {
        return Plot.builder()
                .name(request.getName())
                .description(request.getDescription())
                .location(request.getLocation())
                .areaHectares(request.getAreaHectares())
                .soilType(request.getSoilType())
                .userId(request.getUserId())
                .registrationDate(LocalDate.now())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();
    }

    public PlotResponseDTO toResponseDTO(Plot plot) {
        return PlotResponseDTO.builder()
                .id(plot.getId())
                .code(plot.getCode())
                .name(plot.getName())
                .description(plot.getDescription())
                .location(plot.getLocation())
                .areaHectares(plot.getAreaHectares())
                .soilType(plot.getSoilType())
                .userId(plot.getUserId())
                .registrationDate(plot.getRegistrationDate())
                .isActive(plot.getIsActive())
                .createdAt(plot.getCreatedAt())
                .updatedAt(plot.getUpdatedAt())
                .build();
    }
}