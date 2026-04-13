package pe.agro.weatherservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.agro.weatherservice.dto.WeatherRequestDTO;
import pe.agro.weatherservice.dto.WeatherResponseDTO;
import pe.agro.weatherservice.service.WeatherService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherRest {

    private final WeatherService weatherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WeatherResponseDTO> save(@Valid @RequestBody WeatherRequestDTO request) {
        return weatherService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WeatherResponseDTO> update(@PathVariable Long id, @Valid @RequestBody WeatherRequestDTO request) {
        return weatherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return weatherService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WeatherResponseDTO> findById(@PathVariable Long id) {
        return weatherService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<WeatherResponseDTO> findAll() {
        return weatherService.findAll();
    }

    @GetMapping("/plot/{plotId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<WeatherResponseDTO> findAllByPlotId(@PathVariable Long plotId) {
        return weatherService.findAllByPlotId(plotId);
    }

    @GetMapping("/date-range")
    @ResponseStatus(HttpStatus.OK)
    public Flux<WeatherResponseDTO> findAllByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return weatherService.findAllByDateRange(startDate, endDate);
    }

    @GetMapping("/plot/{plotId}/date-range")
    @ResponseStatus(HttpStatus.OK)
    public Flux<WeatherResponseDTO> findAllByPlotIdAndDateRange(
            @PathVariable Long plotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return weatherService.findAllByPlotIdAndDateRange(plotId, startDate, endDate);
    }

    @GetMapping("/plot/{plotId}/last")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WeatherResponseDTO> findLastWeatherByPlotId(@PathVariable Long plotId) {
        return weatherService.findLastWeatherByPlotId(plotId);
    }
}