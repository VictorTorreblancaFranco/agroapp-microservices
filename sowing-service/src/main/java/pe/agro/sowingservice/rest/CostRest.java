package pe.agro.sowingservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.sowingservice.dto.CostRequestDTO;
import pe.agro.sowingservice.dto.CostResponseDTO;
import pe.agro.sowingservice.service.CostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/costs")
@RequiredArgsConstructor
public class CostRest {

    private final CostService costService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CostResponseDTO> save(@Valid @RequestBody CostRequestDTO request) {
        return costService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CostResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CostRequestDTO request) {
        return costService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return costService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CostResponseDTO> findById(@PathVariable Long id) {
        return costService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CostResponseDTO> findAll() {
        return costService.findAll();
    }

    @GetMapping("/sowing/{sowingId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CostResponseDTO> findAllBySowingId(@PathVariable Long sowingId) {
        return costService.findAllBySowingId(sowingId);
    }

    @GetMapping("/sowing/{sowingId}/total")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Double> getTotalCostBySowingId(@PathVariable Long sowingId) {
        return costService.getTotalCostBySowingId(sowingId);
    }
}