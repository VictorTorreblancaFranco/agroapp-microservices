package pe.agro.farmservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.farmservice.dto.PlotRequestDTO;
import pe.agro.farmservice.dto.PlotResponseDTO;
import pe.agro.farmservice.mapper.PlotMapper;
import pe.agro.farmservice.repository.PlotRepository;
import pe.agro.farmservice.service.PlotEventPublisher;
import pe.agro.farmservice.service.PlotService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import pe.agro.farmservice.model.Plot;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlotServiceImpl implements PlotService {

    private final PlotRepository plotRepository;
    private final PlotMapper plotMapper;
    private final PlotEventPublisher eventPublisher;

    @Override
    public Mono<PlotResponseDTO> save(PlotRequestDTO request) {
        log.info("Saving new plot: {}", request.getName());

        return validateUniqueConstraints(request)
                .then(Mono.defer(() -> {
                    var plot = plotMapper.toEntity(request);
                    return plotRepository.save(plot);
                }))
                .map(this::generateCode)
                .flatMap(plotRepository::save)
                .map(plotMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("Plot saved with id: {}, code: {}", response.getId(), response.getCode());
                    eventPublisher.publishCreated(response);
                })
                .doOnError(error -> log.error("Error saving plot: {}", error.getMessage()));
    }

    @Override
    public Mono<PlotResponseDTO> update(Long id, PlotRequestDTO request) {
        log.info("Updating plot with id: {}", id);

        return plotRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Plot not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setName(request.getName());
                    existing.setDescription(request.getDescription());
                    existing.setLocation(request.getLocation());
                    existing.setAreaHectares(request.getAreaHectares());
                    existing.setSoilType(request.getSoilType());
                    existing.setUpdatedAt(java.time.LocalDateTime.now());
                    return plotRepository.save(existing);
                })
                .map(plotMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("Plot updated: {}", response.getId());
                    eventPublisher.publishUpdated(response);
                });
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Soft deleting plot with id: {}", id);

        return plotRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Plot not found with id: " + id)))
                .flatMap(plot -> plotRepository.softDeleteById(id)
                        .doOnSuccess(v -> eventPublisher.publishDeleted(id, plot.getCode(), plot.getUserId())))
                .then();
    }

    @Override
    public Mono<PlotResponseDTO> restore(Long id) {
        log.info("Restoring plot with id: {}", id);
        return plotRepository.restoreById(id)
                .then(plotRepository.findById(id))
                .map(plotMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Plot restored: {}", response.getId()));
    }

    @Override
    public Mono<PlotResponseDTO> findById(Long id) {
        log.info("Finding plot by id: {}", id);
        return plotRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Plot not found with id: " + id)))
                .map(plotMapper::toResponseDTO);
    }

    @Override
    public Mono<PlotResponseDTO> findByCode(String code) {
        log.info("Finding plot by code: {}", code);
        return plotRepository.findByCode(code)
                .switchIfEmpty(Mono.error(new RuntimeException("Plot not found with code: " + code)))
                .map(plotMapper::toResponseDTO);
    }

    @Override
    public Flux<PlotResponseDTO> findAll() {
        log.info("Finding all plots");
        return plotRepository.findAll()
                .map(plotMapper::toResponseDTO);
    }

    @Override
    public Flux<PlotResponseDTO> findAllByUserId(Long userId) {
        log.info("Finding plots by user id: {}", userId);
        return plotRepository.findByUserId(userId)
                .map(plotMapper::toResponseDTO);
    }

    @Override
    public Flux<PlotResponseDTO> findAllActive() {
        log.info("Finding all active plots");
        return plotRepository.findByIsActiveTrue()
                .map(plotMapper::toResponseDTO);
    }

    @Override
    public Flux<PlotResponseDTO> findAllInactive() {
        log.info("Finding all inactive plots");
        return plotRepository.findByIsActiveFalse()
                .map(plotMapper::toResponseDTO);
    }

    private Plot generateCode(Plot plot) {
        String code = String.format("P-%04d", plot.getId());
        plot.setCode(code);
        return plot;
    }

    private Mono<Void> validateUniqueConstraints(PlotRequestDTO request) {
        return plotRepository.existsByNameAndUserId(request.getName(), request.getUserId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Plot already exists with name: " + request.getName()));
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Boolean> existsByNameAndUserId(String name, Long userId) {
        return plotRepository.existsByNameAndUserId(name, userId);
    }
}