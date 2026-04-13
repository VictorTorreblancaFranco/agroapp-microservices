package pe.agro.sowingservice.service;

import pe.agro.sowingservice.dto.LossRequestDTO;
import pe.agro.sowingservice.dto.LossResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LossService {
    Mono<LossResponseDTO> save(LossRequestDTO request);
    Mono<LossResponseDTO> update(Long id, LossRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<LossResponseDTO> findById(Long id);
    Flux<LossResponseDTO> findAll();
    Flux<LossResponseDTO> findAllBySowingId(Long sowingId);
    Mono<Integer> getTotalLossBySowingId(Long sowingId);
}