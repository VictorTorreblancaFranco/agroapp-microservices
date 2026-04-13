package pe.agro.sowingservice.service;

import pe.agro.sowingservice.dto.CostRequestDTO;
import pe.agro.sowingservice.dto.CostResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CostService {
    Mono<CostResponseDTO> save(CostRequestDTO request);
    Mono<CostResponseDTO> update(Long id, CostRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<CostResponseDTO> findById(Long id);
    Flux<CostResponseDTO> findAll();
    Flux<CostResponseDTO> findAllBySowingId(Long sowingId);
    Mono<Double> getTotalCostBySowingId(Long sowingId);
}