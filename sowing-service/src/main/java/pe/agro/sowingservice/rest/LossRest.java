package pe.agro.sowingservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.sowingservice.dto.LossRequestDTO;
import pe.agro.sowingservice.dto.LossResponseDTO;
import pe.agro.sowingservice.service.LossService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/losses")
@RequiredArgsConstructor
public class LossRest {

    private final LossService lossService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LossResponseDTO> save(@Valid @RequestBody LossRequestDTO request) {
        return lossService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LossResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LossRequestDTO request) {
        return lossService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return lossService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LossResponseDTO> findById(@PathVariable Long id) {
        return lossService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<LossResponseDTO> findAll() {
        return lossService.findAll();
    }

    @GetMapping("/sowing/{sowingId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<LossResponseDTO> findAllBySowingId(@PathVariable Long sowingId) {
        return lossService.findAllBySowingId(sowingId);
    }

    @GetMapping("/sowing/{sowingId}/total")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> getTotalLossBySowingId(@PathVariable Long sowingId) {
        return lossService.getTotalLossBySowingId(sowingId);
    }
}