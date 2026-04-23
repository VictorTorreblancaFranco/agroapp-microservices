package pe.agro.harvestservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.harvestservice.dto.HarvestRequestDTO;
import pe.agro.harvestservice.dto.HarvestResponseDTO;
import pe.agro.harvestservice.model.Harvest;
import java.time.LocalDateTime;

@Component
public class HarvestMapper {
    
    public Harvest toEntity(HarvestRequestDTO request) {
        return Harvest.builder()
                .harvestDate(request.getHarvestDate())
                .quantity(request.getQuantity())
                .quality(request.getQuality())
                .observations(request.getObservations())
                .sowingId(request.getSowingId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    public HarvestResponseDTO toResponseDTO(Harvest harvest) {
        return HarvestResponseDTO.builder()
                .id(harvest.getId())
                .harvestDate(harvest.getHarvestDate())
                .quantity(harvest.getQuantity())
                .quality(harvest.getQuality())
                .observations(harvest.getObservations())
                .sowingId(harvest.getSowingId())
                .createdAt(harvest.getCreatedAt())
                .build();
    }
}
