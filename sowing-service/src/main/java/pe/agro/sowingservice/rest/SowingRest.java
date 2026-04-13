package pe.agro.sowingservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.sowingservice.dto.SowingRequestDTO;
import pe.agro.sowingservice.dto.SowingResponseDTO;
import pe.agro.sowingservice.service.SowingService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/sowings")
@RequiredArgsConstructor
public class SowingRest {

    private final SowingService sowingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SowingResponseDTO> save(@Valid @RequestBody SowingRequestDTO request) {
        return sowingService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SowingResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SowingRequestDTO request) {
        return sowingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return sowingService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SowingResponseDTO> findById(@PathVariable Long id) {
        return sowingService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<SowingResponseDTO> findAll() {
        return sowingService.findAll();
    }

    @GetMapping("/plot/{plotId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SowingResponseDTO> findAllByPlotId(@PathVariable Long plotId) {
        return sowingService.findAllByPlotId(plotId);
    }

    @GetMapping("/crop/{cropId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SowingResponseDTO> findAllByCropId(@PathVariable Long cropId) {
        return sowingService.findAllByCropId(cropId);
    }

    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SowingResponseDTO> findAllByStatus(@PathVariable String status) {
        return sowingService.findAllByStatus(status);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SowingResponseDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return sowingService.updateStatus(id, status);
    }

    @PatchMapping("/{id}/complete")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SowingResponseDTO> completeHarvest(@PathVariable Long id, @RequestParam LocalDate harvestDate) {
        return sowingService.completeHarvest(id, harvestDate);
    }
}