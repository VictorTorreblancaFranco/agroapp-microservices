package pe.agro.weatherservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.agro.weatherservice.dto.WeatherRequestDTO;
import pe.agro.weatherservice.dto.WeatherResponseDTO;
import pe.agro.weatherservice.mapper.WeatherMapper;
import pe.agro.weatherservice.repository.WeatherRepository;
import pe.agro.weatherservice.service.WeatherEventPublisher;
import pe.agro.weatherservice.service.WeatherService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final WeatherEventPublisher eventPublisher;

    @Override
    public Mono<WeatherResponseDTO> save(WeatherRequestDTO request) {
        log.info("Saving weather record for plot: {}", request.getPlotId());

        return Mono.defer(() -> {
                    var weather = weatherMapper.toEntity(request);
                    return weatherRepository.save(weather);
                })
                .map(weatherMapper::toResponseDTO)
                .doOnSuccess(response -> {
                    log.info("Weather saved with id: {}", response.getId());
                    eventPublisher.publishCreated(response);
                })
                .doOnError(error -> log.error("Error saving weather: {}", error.getMessage()));
    }

    @Override
    public Mono<WeatherResponseDTO> update(Long id, WeatherRequestDTO request) {
        log.info("Updating weather record with id: {}", id);

        return weatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Weather not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setDate(request.getDate());
                    existing.setTemperature(request.getTemperature());
                    existing.setHumidity(request.getHumidity());
                    existing.setRainfall(request.getRainfall());
                    existing.setWind(request.getWind());
                    existing.setPlotId(request.getPlotId());
                    return weatherRepository.save(existing);
                })
                .map(weatherMapper::toResponseDTO)
                .doOnSuccess(response -> eventPublisher.publishUpdated(response));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting weather record with id: {}", id);
        return weatherRepository.deleteById(id);
    }

    @Override
    public Mono<WeatherResponseDTO> findById(Long id) {
        log.info("Finding weather by id: {}", id);
        return weatherRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Weather not found with id: " + id)))
                .map(weatherMapper::toResponseDTO);
    }

    @Override
    public Flux<WeatherResponseDTO> findAll() {
        log.info("Finding all weather records");
        return weatherRepository.findAll()
                .map(weatherMapper::toResponseDTO);
    }

    @Override
    public Flux<WeatherResponseDTO> findAllByPlotId(Long plotId) {
        log.info("Finding weather by plot id: {}", plotId);
        return weatherRepository.findByPlotId(plotId)
                .map(weatherMapper::toResponseDTO);
    }

    @Override
    public Flux<WeatherResponseDTO> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Finding weather by date range: {} to {}", startDate, endDate);
        return weatherRepository.findByDateBetween(startDate, endDate)
                .map(weatherMapper::toResponseDTO);
    }

    @Override
    public Flux<WeatherResponseDTO> findAllByPlotIdAndDateRange(Long plotId, LocalDate startDate, LocalDate endDate) {
        log.info("Finding weather by plot {} and date range: {} to {}", plotId, startDate, endDate);
        return weatherRepository.findByPlotIdAndDateBetween(plotId, startDate, endDate)
                .map(weatherMapper::toResponseDTO);
    }

    @Override
    public Mono<WeatherResponseDTO> findLastWeatherByPlotId(Long plotId) {
        log.info("Finding last weather record for plot: {}", plotId);
        return weatherRepository.findLastWeatherByPlotId(plotId)
                .map(weatherMapper::toResponseDTO);
    }
}