package pe.agro.sowingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.sowingservice.dto.CostRequestDTO;
import pe.agro.sowingservice.dto.CostResponseDTO;
import pe.agro.sowingservice.mapper.CostMapper;
import pe.agro.sowingservice.repository.CostRepository;
import pe.agro.sowingservice.service.CostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;
    private final CostMapper costMapper;

    @Override
    public Mono<CostResponseDTO> save(CostRequestDTO request) {
        log.info("Saving cost for sowing: {}", request.getSowingId());

        return Mono.defer(() -> {
                    var cost = costMapper.toEntity(request);
                    return costRepository.save(cost);
                })
                .map(costMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Cost saved with id: {}", response.getId()));
    }

    @Override
    public Mono<CostResponseDTO> update(Long id, CostRequestDTO request) {
        log.info("Updating cost with id: {}", id);

        return costRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cost not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setDescription(request.getDescription());
                    existing.setType(request.getType());
                    existing.setAmount(request.getAmount());
                    existing.setDate(request.getDate());
                    existing.setPaymentMethod(request.getPaymentMethod());
                    return costRepository.save(existing);
                })
                .map(costMapper::toResponseDTO);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting cost with id: {}", id);
        return costRepository.deleteById(id);
    }

    @Override
    public Mono<CostResponseDTO> findById(Long id) {
        log.info("Finding cost by id: {}", id);
        return costRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Cost not found with id: " + id)))
                .map(costMapper::toResponseDTO);
    }

    @Override
    public Flux<CostResponseDTO> findAll() {
        log.info("Finding all costs");
        return costRepository.findAll()
                .map(costMapper::toResponseDTO);
    }

    @Override
    public Flux<CostResponseDTO> findAllBySowingId(Long sowingId) {
        log.info("Finding costs by sowing id: {}", sowingId);
        return costRepository.findBySowingId(sowingId)
                .map(costMapper::toResponseDTO);
    }

    @Override
    public Mono<Double> getTotalCostBySowingId(Long sowingId) {
        log.info("Getting total cost by sowing id: {}", sowingId);
        return costRepository.sumAmountBySowingId(sowingId)
                .defaultIfEmpty(0.0);
    }
}