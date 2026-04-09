package pe.agro.cropservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.cropservice.dto.CropRequestDTO;
import pe.agro.cropservice.dto.CropResponseDTO;
import pe.agro.cropservice.dto.CropUpdateDTO;
import pe.agro.cropservice.mapper.CropMapper;
import pe.agro.cropservice.repository.CropRepository;
import pe.agro.cropservice.service.CropService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final CropMapper cropMapper;

    @Override
    public Mono<CropResponseDTO> save(CropRequestDTO request) {
        log.info("Saving new crop: {}", request.getName());

        return validateUniqueConstraints(request)
                .then(Mono.defer(() -> {
                    var crop = cropMapper.toEntity(request);
                    return cropRepository.save(crop);
                }))
                .map(cropMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Crop saved successfully with id: {}", response.getId()))
                .doOnError(error -> log.error("Error saving crop: {}", error.getMessage()));
    }

    @Override
    public Mono<CropResponseDTO> update(CropUpdateDTO request) {
        log.info("Updating crop with id: {}", request.getId());

        return cropRepository.findById(request.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Crop not found with id: " + request.getId())))
                .map(existing -> cropMapper.toEntity(request, existing))
                .flatMap(cropRepository::save)
                .map(cropMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Crop updated successfully: {}", response.getId()))
                .doOnError(error -> log.error("Error updating crop: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Soft deleting crop with id: {}", id);

        return cropRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Crop not found with id: " + id)))
                .flatMap(crop -> {
                    crop.setIsActive(false);
                    return cropRepository.save(crop);
                })
                .then();
    }

    @Override
    public Mono<CropResponseDTO> restore(Long id) {
        log.info("Restoring crop with id: {}", id);

        return cropRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Crop not found with id: " + id)))
                .flatMap(crop -> {
                    crop.setIsActive(true);
                    return cropRepository.save(crop);
                })
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Mono<CropResponseDTO> findById(Long id) {
        log.info("Finding crop by id: {}", id);
        return cropRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Crop not found with id: " + id)))
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Mono<CropResponseDTO> findByName(String name) {
        log.info("Finding crop by name: {}", name);
        return cropRepository.findByName(name)
                .switchIfEmpty(Mono.error(new RuntimeException("Crop not found with name: " + name)))
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Flux<CropResponseDTO> findAll() {
        log.info("Finding all crops");
        return cropRepository.findAll()
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Flux<CropResponseDTO> findAllActive() {
        log.info("Finding all active crops");
        return cropRepository.findByIsActiveTrue()
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Flux<CropResponseDTO> findAllInactive() {
        log.info("Finding all inactive crops");
        return cropRepository.findByIsActiveFalse()
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Flux<CropResponseDTO> findByType(String type) {
        log.info("Finding crops by type: {}", type);
        return cropRepository.findByType(type)
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Flux<CropResponseDTO> findBySeason(String season) {
        log.info("Finding crops by season: {}", season);
        return cropRepository.findBySeason(season)
                .map(cropMapper::toResponseDTO);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return cropRepository.existsByName(name);
    }

    private Mono<Void> validateUniqueConstraints(CropRequestDTO request) {
        return cropRepository.existsByName(request.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Crop already exists with name: " + request.getName()));
                    }
                    return Mono.empty();
                });
    }
}