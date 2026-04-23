package pe.agro.harvestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.harvestservice.dto.HarvestRequestDTO;
import pe.agro.harvestservice.dto.HarvestResponseDTO;
import pe.agro.harvestservice.mapper.HarvestMapper;
import pe.agro.harvestservice.repository.HarvestRepository;
import pe.agro.harvestservice.service.HarvestEventPublisher;
import pe.agro.harvestservice.service.HarvestService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {

    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;
    private final HarvestEventPublisher eventPublisher;

    @Override
    public Mono<HarvestResponseDTO> save(HarvestRequestDTO request) {
        log.info("Saving harvest for sowing: {}", request.getSowingId());
        
        return Mono.defer(() -> {
            var harvest = harvestMapper.toEntity(request);
            return harvestRepository.save(harvest);
        })
        .map(harvestMapper::toResponseDTO)
        .doOnSuccess(response -> {
            log.info("Harvest saved with id: {}", response.getId());
            eventPublisher.publishCreated(response);
        })
        .doOnError(error -> log.error("Error saving harvest: {}", error.getMessage()));
    }

    @Override
    public Mono<HarvestResponseDTO> update(Long id, HarvestRequestDTO request) {
        log.info("Updating harvest with id: {}", id);
        
        return harvestRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Harvest not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setHarvestDate(request.getHarvestDate());
                    existing.setQuantity(request.getQuantity());
                    existing.setQuality(request.getQuality());
                    existing.setObservations(request.getObservations());
                    existing.setSowingId(request.getSowingId());
                    return harvestRepository.save(existing);
                })
                .map(harvestMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishUpdated(response));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting harvest with id: {}", id);
        return harvestRepository.deleteById(id);
    }

    @Override
    public Mono<HarvestResponseDTO> findById(Long id) {
        log.info("Finding harvest by id: {}", id);
        return harvestRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Harvest not found with id: " + id)))
                .map(harvestMapper::toResponseDTO);
    }

    @Override
    public Flux<HarvestResponseDTO> findAll() {
        log.info("Finding all harvests");
        return harvestRepository.findAll()
                .map(harvestMapper::toResponseDTO);
    }

    @Override
    public Flux<HarvestResponseDTO> findAllBySowingId(Long sowingId) {
        log.info("Finding harvests by sowing id: {}", sowingId);
        return harvestRepository.findBySowingId(sowingId)
                .map(harvestMapper::toResponseDTO);
    }
}
