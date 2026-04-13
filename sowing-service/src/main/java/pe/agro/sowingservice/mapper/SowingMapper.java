package pe.agro.sowingservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.sowingservice.dto.SowingRequestDTO;
import pe.agro.sowingservice.dto.SowingResponseDTO;
import pe.agro.sowingservice.model.Sowing;
import java.time.LocalDateTime;

@Component
public class SowingMapper {

    public Sowing toEntity(SowingRequestDTO request) {
        return Sowing.builder()
                .sowingDate(request.getSowingDate())
                .estimatedHarvestDate(request.getEstimatedHarvestDate())
                .seedQuantity(request.getSeedQuantity())
                .observations(request.getObservations())
                .cropId(request.getCropId())
                .plotId(request.getPlotId())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(1)
                .build();
    }

    public SowingResponseDTO toResponseDTO(Sowing sowing) {
        return SowingResponseDTO.builder()
                .id(sowing.getId())
                .sowingDate(sowing.getSowingDate())
                .estimatedHarvestDate(sowing.getEstimatedHarvestDate())
                .actualHarvestDate(sowing.getActualHarvestDate())
                .seedQuantity(sowing.getSeedQuantity())
                .status(sowing.getStatus())
                .observations(sowing.getObservations())
                .cropId(sowing.getCropId())
                .plotId(sowing.getPlotId())
                .createdAt(sowing.getCreatedAt())
                .updatedAt(sowing.getUpdatedAt())
                .build();
    }
}