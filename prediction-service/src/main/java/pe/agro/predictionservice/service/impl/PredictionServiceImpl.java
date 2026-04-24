package pe.agro.predictionservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.predictionservice.dto.PredictionRequestDTO;
import pe.agro.predictionservice.dto.PredictionResponseDTO;
import pe.agro.predictionservice.mapper.PredictionMapper;
import pe.agro.predictionservice.repository.PredictionRepository;
import pe.agro.predictionservice.service.PredictionEventPublisher;
import pe.agro.predictionservice.service.PredictionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {
    private final PredictionRepository predictionRepository;
    private final PredictionMapper predictionMapper;
    private final PredictionEventPublisher eventPublisher;

    @Override
    public Mono<PredictionResponseDTO> save(PredictionRequestDTO request) {
        log.info("Saving prediction for sowing: {}", request.getSowingId());
        return Mono.defer(() -> {
            var prediction = predictionMapper.toEntity(request);
            return predictionRepository.save(prediction);
        })
        .map(predictionMapper::toResponseDTO)
        .doOnSuccess(response -> {
            log.info("Prediction saved with id: {}", response.getId());
            eventPublisher.publishCreated(response);
        });
    }

    @Override
    public Mono<PredictionResponseDTO> update(Long id, PredictionRequestDTO request) {
        log.info("Updating prediction with id: {}", id);
        return predictionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Prediction not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setDate(request.getDate());
                    existing.setEstimatedYield(request.getEstimatedYield());
                    existing.setSuccessProbability(request.getSuccessProbability());
                    existing.setMethod(request.getMethod());
                    existing.setObservations(request.getObservations());
                    existing.setSowingId(request.getSowingId());
                    return predictionRepository.save(existing);
                })
                .map(predictionMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishUpdated(response));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting prediction with id: {}", id);
        return predictionRepository.deleteById(id);
    }

    @Override
    public Mono<PredictionResponseDTO> findById(Long id) {
        log.info("Finding prediction by id: {}", id);
        return predictionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Prediction not found with id: " + id)))
                .map(predictionMapper::toResponseDTO);
    }

    @Override
    public Flux<PredictionResponseDTO> findAll() {
        log.info("Finding all predictions");
        return predictionRepository.findAll().map(predictionMapper::toResponseDTO);
    }

    @Override
    public Flux<PredictionResponseDTO> findAllBySowingId(Long sowingId) {
        log.info("Finding predictions by sowing id: {}", sowingId);
        return predictionRepository.findBySowingId(sowingId).map(predictionMapper::toResponseDTO);
    }
}
