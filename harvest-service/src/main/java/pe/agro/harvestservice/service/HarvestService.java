package pe.agro.harvestservice.service;

import pe.agro.harvestservice.dto.HarvestRequestDTO;
import pe.agro.harvestservice.dto.HarvestResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HarvestService {
    Mono<HarvestResponseDTO> save(HarvestRequestDTO request);
    Mono<HarvestResponseDTO> update(Long id, HarvestRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<HarvestResponseDTO> findById(Long id);
    Flux<HarvestResponseDTO> findAll();
    Flux<HarvestResponseDTO> findAllBySowingId(Long sowingId);
}
