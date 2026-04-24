package pe.agro.predictionservice.mapper;

import org.springframework.stereotype.Component;
import pe.agro.predictionservice.dto.PredictionRequestDTO;
import pe.agro.predictionservice.dto.PredictionResponseDTO;
import pe.agro.predictionservice.model.Prediction;
import java.time.LocalDateTime;

@Component
public class PredictionMapper {
    public Prediction toEntity(PredictionRequestDTO request) {
        return Prediction.builder()
                .date(request.getDate())
                .estimatedYield(request.getEstimatedYield())
                .successProbability(request.getSuccessProbability())
                .method(request.getMethod())
                .observations(request.getObservations())
                .sowingId(request.getSowingId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public PredictionResponseDTO toResponseDTO(Prediction prediction) {
        return PredictionResponseDTO.builder()
                .id(prediction.getId())
                .date(prediction.getDate())
                .estimatedYield(prediction.getEstimatedYield())
                .successProbability(prediction.getSuccessProbability())
                .method(prediction.getMethod())
                .observations(prediction.getObservations())
                .sowingId(prediction.getSowingId())
                .createdAt(prediction.getCreatedAt())
                .build();
    }
}
