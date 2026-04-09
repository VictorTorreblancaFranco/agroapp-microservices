package pe.agro.cropservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.cropservice.dto.CropRequestDTO;
import pe.agro.cropservice.dto.CropResponseDTO;
import pe.agro.cropservice.dto.CropUpdateDTO;
import pe.agro.cropservice.service.CropService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/crops")
@RequiredArgsConstructor
public class CropRest {

    private final CropService cropService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CropResponseDTO> save(@Valid @RequestBody CropRequestDTO request) {
        return cropService.save(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<CropResponseDTO> update(@Valid @RequestBody CropUpdateDTO request) {
        return cropService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return cropService.deleteById(id);
    }

    @PatchMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CropResponseDTO> restore(@PathVariable Long id) {
        return cropService.restore(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CropResponseDTO> findById(@PathVariable Long id) {
        return cropService.findById(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CropResponseDTO> findByName(@PathVariable String name) {
        return cropService.findByName(name);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CropResponseDTO> findAll() {
        return cropService.findAll();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CropResponseDTO> findAllActive() {
        return cropService.findAllActive();
    }

    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CropResponseDTO> findAllInactive() {
        return cropService.findAllInactive();
    }

    @GetMapping("/type/{type}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CropResponseDTO> findByType(@PathVariable String type) {
        return cropService.findByType(type);
    }

    @GetMapping("/season/{season}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CropResponseDTO> findBySeason(@PathVariable String season) {
        return cropService.findBySeason(season);
    }

    @GetMapping("/exists/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> existsByName(@PathVariable String name) {
        return cropService.existsByName(name);
    }
}