package pe.agro.sowingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.sowingservice.dto.SowingRequestDTO;
import pe.agro.sowingservice.dto.SowingResponseDTO;
import pe.agro.sowingservice.mapper.SowingMapper;
import pe.agro.sowingservice.repository.SowingRepository;
import pe.agro.sowingservice.service.SowingEventPublisher;
import pe.agro.sowingservice.service.SowingService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SowingServiceImpl implements SowingService {

    private final SowingRepository sowingRepository;
    private final SowingMapper sowingMapper;
    private final SowingEventPublisher eventPublisher;

    @Override
    public Mono<SowingResponseDTO> save(SowingRequestDTO request) {
        log.info("Saving new sowing for plot: {} and crop: {}", request.getPlotId(), request.getCropId());

        return Mono.defer(() -> {
                    var sowing = sowingMapper.toEntity(request);
                    return sowingRepository.save(sowing);
                })
                .map(sowingMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("Sowing saved with id: {}", response.getId());
                    eventPublisher.publishCreated(response);
                })
                .doOnError(error -> log.error("Error saving sowing: {}", error.getMessage()));
    }

    @Override
    public Mono<SowingResponseDTO> update(Long id, SowingRequestDTO request) {
        log.info("Updating sowing with id: {}", id);

        return sowingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Sowing not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setSowingDate(request.getSowingDate());
                    existing.setEstimatedHarvestDate(request.getEstimatedHarvestDate());
                    existing.setSeedQuantity(request.getSeedQuantity());
                    existing.setObservations(request.getObservations());
                    existing.setCropId(request.getCropId());
                    existing.setPlotId(request.getPlotId());
                    existing.setUpdatedAt(java.time.LocalDateTime.now());
                    return sowingRepository.save(existing);
                })
                .map(sowingMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishUpdated(response));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting sowing with id: {}", id);
        return sowingRepository.deleteById(id);
    }

    @Override
    public Mono<SowingResponseDTO> findById(Long id) {
        log.info("Finding sowing by id: {}", id);
        return sowingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Sowing not found with id: " + id)))
                .map(sowingMapper::toResponseDTO);
    }

    @Override
    public Flux<SowingResponseDTO> findAll() {
        log.info("Finding all sowings");
        return sowingRepository.findAll()
                .map(sowingMapper::toResponseDTO);
    }

    @Override
    public Flux<SowingResponseDTO> findAllByPlotId(Long plotId) {
        log.info("Finding sowings by plot id: {}", plotId);
        return sowingRepository.findByPlotId(plotId)
                .map(sowingMapper::toResponseDTO);
    }

    @Override
    public Flux<SowingResponseDTO> findAllByCropId(Long cropId) {
        log.info("Finding sowings by crop id: {}", cropId);
        return sowingRepository.findByCropId(cropId)
                .map(sowingMapper::toResponseDTO);
    }

    @Override
    public Flux<SowingResponseDTO> findAllByStatus(String status) {
        log.info("Finding sowings by status: {}", status);
        return sowingRepository.findByStatus(status)
                .map(sowingMapper::toResponseDTO);
    }

    @Override
    public Mono<SowingResponseDTO> updateStatus(Long id, String status) {
        log.info("Updating sowing status: {} -> {}", id, status);

        return sowingRepository.updateStatus(id, status)
                .then(sowingRepository.findById(id))
                .map(sowingMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishStatusChanged(response, status));
    }

    @Override
    public Mono<SowingResponseDTO> completeHarvest(Long id, LocalDate harvestDate) {
        log.info("Completing harvest for sowing: {} with date: {}", id, harvestDate);

        return sowingRepository.completeHarvest(id, harvestDate)
                .then(sowingRepository.findById(id))
                .map(sowingMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishCompleted(response));
    }
}