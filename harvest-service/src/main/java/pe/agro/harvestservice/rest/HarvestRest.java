package pe.agro.harvestservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.harvestservice.dto.HarvestRequestDTO;
import pe.agro.harvestservice.dto.HarvestResponseDTO;
import pe.agro.harvestservice.service.HarvestService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
public class HarvestRest {

    private final HarvestService harvestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<HarvestResponseDTO> save(@Valid @RequestBody HarvestRequestDTO request) {
        return harvestService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<HarvestResponseDTO> update(@PathVariable Long id, @Valid @RequestBody HarvestRequestDTO request) {
        return harvestService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return harvestService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<HarvestResponseDTO> findById(@PathVariable Long id) {
        return harvestService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<HarvestResponseDTO> findAll() {
        return harvestService.findAll();
    }

    @GetMapping("/sowing/{sowingId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<HarvestResponseDTO> findAllBySowingId(@PathVariable Long sowingId) {
        return harvestService.findAllBySowingId(sowingId);
    }
}
