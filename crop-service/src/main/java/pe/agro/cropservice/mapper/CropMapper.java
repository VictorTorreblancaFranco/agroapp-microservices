package pe.agro.cropservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.cropservice.dto.CropRequestDTO;
import pe.agro.cropservice.dto.CropResponseDTO;
import pe.agro.cropservice.dto.CropUpdateDTO;
import pe.agro.cropservice.model.Crop;
import java.time.LocalDateTime;

@Component
public class CropMapper {

    public Crop toEntity(CropRequestDTO request) {
        if (request == null) {
            return null;
        }

        return Crop.builder()
                .name(request.getName())
                .scientificName(request.getScientificName())
                .type(request.getType())
                .variety(request.getVariety())
                .description(request.getDescription())
                .season(request.getSeason())
                .cycleDays(request.getCycleDays())
                .expectedYield(request.getExpectedYield())
                .sowingDensity(request.getSowingDensity())
                .optimalTempMin(request.getOptimalTempMin())
                .optimalTempMax(request.getOptimalTempMax())
                .optimalHumidityMin(request.getOptimalHumidityMin())
                .optimalHumidityMax(request.getOptimalHumidityMax())
                .isActive(true)
                .version(1)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Crop toEntity(CropUpdateDTO request, Crop existing) {
        if (request == null || existing == null) {
            return existing;
        }

        existing.setName(request.getName());
        existing.setScientificName(request.getScientificName());
        existing.setType(request.getType());
        existing.setVariety(request.getVariety());
        existing.setDescription(request.getDescription());
        existing.setSeason(request.getSeason());
        existing.setCycleDays(request.getCycleDays());
        existing.setExpectedYield(request.getExpectedYield());
        existing.setSowingDensity(request.getSowingDensity());
        existing.setOptimalTempMin(request.getOptimalTempMin());
        existing.setOptimalTempMax(request.getOptimalTempMax());
        existing.setOptimalHumidityMin(request.getOptimalHumidityMin());
        existing.setOptimalHumidityMax(request.getOptimalHumidityMax());

        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }

        return existing;
    }

    public CropResponseDTO toResponseDTO(Crop crop) {
        if (crop == null) {
            return null;
        }

        return CropResponseDTO.builder()
                .id(crop.getId())
                .name(crop.getName())
                .scientificName(crop.getScientificName())
                .type(crop.getType())
                .variety(crop.getVariety())
                .description(crop.getDescription())
                .season(crop.getSeason())
                .cycleDays(crop.getCycleDays())
                .expectedYield(crop.getExpectedYield())
                .sowingDensity(crop.getSowingDensity())
                .optimalTempMin(crop.getOptimalTempMin())
                .optimalTempMax(crop.getOptimalTempMax())
                .optimalHumidityMin(crop.getOptimalHumidityMin())
                .optimalHumidityMax(crop.getOptimalHumidityMax())
                .isActive(crop.getIsActive())
                .createdAt(crop.getCreatedAt())
                .updatedAt(crop.getUpdatedAt())
                .build();
    }
}