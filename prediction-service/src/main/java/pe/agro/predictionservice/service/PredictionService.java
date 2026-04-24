package pe.agro.predictionservice.service;

import pe.agro.predictionservice.dto.PredictionRequestDTO;
import pe.agro.predictionservice.dto.PredictionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PredictionService {
    Mono<PredictionResponseDTO> save(PredictionRequestDTO request);
    Mono<PredictionResponseDTO> update(Long id, PredictionRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<PredictionResponseDTO> findById(Long id);
    Flux<PredictionResponseDTO> findAll();
    Flux<PredictionResponseDTO> findAllBySowingId(Long sowingId);
}
