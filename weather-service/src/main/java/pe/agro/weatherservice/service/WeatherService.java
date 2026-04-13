package pe.agro.weatherservice.service;

import pe.agro.weatherservice.dto.WeatherRequestDTO;
import pe.agro.weatherservice.dto.WeatherResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

public interface WeatherService {
    Mono<WeatherResponseDTO> save(WeatherRequestDTO request);
    Mono<WeatherResponseDTO> update(Long id, WeatherRequestDTO request);
    Mono<Void> deleteById(Long id);
    Mono<WeatherResponseDTO> findById(Long id);
    Flux<WeatherResponseDTO> findAll();
    Flux<WeatherResponseDTO> findAllByPlotId(Long plotId);
    Flux<WeatherResponseDTO> findAllByDateRange(LocalDate startDate, LocalDate endDate);
    Flux<WeatherResponseDTO> findAllByPlotIdAndDateRange(Long plotId, LocalDate startDate, LocalDate endDate);
    Mono<WeatherResponseDTO> findLastWeatherByPlotId(Long plotId);
}