package pe.agro.cropservice.service;

import pe.agro.cropservice.dto.CropRequestDTO;
import pe.agro.cropservice.dto.CropResponseDTO;
import pe.agro.cropservice.dto.CropUpdateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CropService {

    Mono<CropResponseDTO> save(CropRequestDTO request);
    Mono<CropResponseDTO> update(CropUpdateDTO request);
    Mono<Void> deleteById(Long id);
    Mono<CropResponseDTO> restore(Long id);
    Mono<CropResponseDTO> findById(Long id);
    Mono<CropResponseDTO> findByName(String name);
    Flux<CropResponseDTO> findAll();
    Flux<CropResponseDTO> findAllActive();
    Flux<CropResponseDTO> findAllInactive();
    Flux<CropResponseDTO> findByType(String type);
    Flux<CropResponseDTO> findBySeason(String season);
    Mono<Boolean> existsByName(String name);
}