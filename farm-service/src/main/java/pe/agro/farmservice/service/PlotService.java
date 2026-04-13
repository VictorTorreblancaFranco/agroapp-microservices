package pe.agro.farmservice.service;

import pe.agro.farmservice.dto.PlotRequestDTO;
import pe.agro.farmservice.dto.PlotResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlotService {
    Mono<PlotResponseDTO> save(PlotRequestDTO request);
    Mono<PlotResponseDTO> update(Long id, PlotRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<PlotResponseDTO> restore(Long id);
    Mono<PlotResponseDTO> findById(Long id);
    Mono<PlotResponseDTO> findByCode(String code);
    Flux<PlotResponseDTO> findAll();
    Flux<PlotResponseDTO> findAllByUserId(Long userId);
    Flux<PlotResponseDTO> findAllActive();
    Flux<PlotResponseDTO> findAllInactive();
    Mono<Boolean> existsByNameAndUserId(String name, Long userId);
}