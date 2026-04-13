package pe.agro.sowingservice.service;

import pe.agro.sowingservice.dto.SowingRequestDTO;
import pe.agro.sowingservice.dto.SowingResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

public interface SowingService {
    Mono<SowingResponseDTO> save(SowingRequestDTO request);
    Mono<SowingResponseDTO> update(Long id, SowingRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<SowingResponseDTO> findById(Long id);
    Flux<SowingResponseDTO> findAll();
    Flux<SowingResponseDTO> findAllByPlotId(Long plotId);
    Flux<SowingResponseDTO> findAllByCropId(Long cropId);
    Flux<SowingResponseDTO> findAllByStatus(String status);
    Mono<SowingResponseDTO> updateStatus(Long id, String status);
    Mono<SowingResponseDTO> completeHarvest(Long id, LocalDate harvestDate);
}