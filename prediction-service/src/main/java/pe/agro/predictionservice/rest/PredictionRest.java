package pe.agro.predictionservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.predictionservice.dto.PredictionRequestDTO;
import pe.agro.predictionservice.dto.PredictionResponseDTO;
import pe.agro.predictionservice.service.PredictionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
public class PredictionRest {
    private final PredictionService predictionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PredictionResponseDTO> save(@Valid @RequestBody PredictionRequestDTO request) {
        return predictionService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PredictionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PredictionRequestDTO request) {
        return predictionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return predictionService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PredictionResponseDTO> findById(@PathVariable Long id) {
        return predictionService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<PredictionResponseDTO> findAll() {
        return predictionService.findAll();
    }

    @GetMapping("/sowing/{sowingId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<PredictionResponseDTO> findAllBySowingId(@PathVariable Long sowingId) {
        return predictionService.findAllBySowingId(sowingId);
    }
}
