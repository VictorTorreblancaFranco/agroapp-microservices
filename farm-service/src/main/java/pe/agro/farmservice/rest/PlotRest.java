package pe.agro.farmservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.farmservice.dto.PlotRequestDTO;
import pe.agro.farmservice.dto.PlotResponseDTO;
import pe.agro.farmservice.service.PlotService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/plots")
@RequiredArgsConstructor
public class PlotRest {

    private final PlotService plotService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlotResponseDTO> save(@Valid @RequestBody PlotRequestDTO request) {
        return plotService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlotResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PlotRequestDTO request) {
        return plotService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return plotService.deleteById(id);
    }

    @PatchMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlotResponseDTO> restore(@PathVariable Long id) {
        return plotService.restore(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlotResponseDTO> findById(@PathVariable Long id) {
        return plotService.findById(id);
    }

    @GetMapping("/code/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlotResponseDTO> findByCode(@PathVariable String code) {
        return plotService.findByCode(code);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlotResponseDTO> findAll() {
        return plotService.findAll();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlotResponseDTO> findAllActive() {
        return plotService.findAllActive();
    }

    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlotResponseDTO> findAllInactive() {
        return plotService.findAllInactive();
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlotResponseDTO> findAllByUserId(@PathVariable Long userId) {
        return plotService.findAllByUserId(userId);
    }

    @GetMapping("/check-name")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> existsByName(@RequestParam String name, @RequestParam Long userId) {
        return plotService.existsByNameAndUserId(name, userId);
    }
}