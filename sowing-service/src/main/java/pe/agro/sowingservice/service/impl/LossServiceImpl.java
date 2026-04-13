package pe.agro.sowingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.sowingservice.dto.LossRequestDTO;
import pe.agro.sowingservice.dto.LossResponseDTO;
import pe.agro.sowingservice.mapper.LossMapper;
import pe.agro.sowingservice.repository.LossRepository;
import pe.agro.sowingservice.service.LossService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LossServiceImpl implements LossService {

    private final LossRepository lossRepository;
    private final LossMapper lossMapper;

    @Override
    public Mono<LossResponseDTO> save(LossRequestDTO request) {
        log.info("Saving loss for sowing: {}", request.getSowingId());

        return Mono.defer(() -> {
                    var loss = lossMapper.toEntity(request);
                    return lossRepository.save(loss);
                })
                .map(lossMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Loss saved with id: {}", response.getId()));
    }

    @Override
    public Mono<LossResponseDTO> update(Long id, LossRequestDTO request) {
        log.info("Updating loss with id: {}", id);

        return lossRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Loss not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setDate(request.getDate());
                    existing.setQuantity(request.getQuantity());
                    existing.setCause(request.getCause());
                    existing.setDescription(request.getDescription());
                    return lossRepository.save(existing);
                })
                .map(lossMapper::toResponseDTO);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting loss with id: {}", id);
        return lossRepository.deleteById(id);
    }

    @Override
    public Mono<LossResponseDTO> findById(Long id) {
        log.info("Finding loss by id: {}", id);
        return lossRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Loss not found with id: " + id)))
                .map(lossMapper::toResponseDTO);
    }

    @Override
    public Flux<LossResponseDTO> findAll() {
        log.info("Finding all losses");
        return lossRepository.findAll()
                .map(lossMapper::toResponseDTO);
    }

    @Override
    public Flux<LossResponseDTO> findAllBySowingId(Long sowingId) {
        log.info("Finding losses by sowing id: {}", sowingId);
        return lossRepository.findBySowingId(sowingId)
                .map(lossMapper::toResponseDTO);
    }

    @Override
    public Mono<Integer> getTotalLossBySowingId(Long sowingId) {
        log.info("Getting total loss by sowing id: {}", sowingId);
        return lossRepository.sumQuantityBySowingId(sowingId)
                .defaultIfEmpty(0);
    }
}