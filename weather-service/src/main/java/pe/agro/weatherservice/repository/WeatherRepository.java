package pe.agro.weatherservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.weatherservice.model.Weather;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Repository
public interface WeatherRepository extends R2dbcRepository<Weather, Long> {

    Flux<Weather> findByPlotId(Long plotId);

    Flux<Weather> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Flux<Weather> findByPlotIdAndDateBetween(Long plotId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT * FROM weather WHERE plot_id = :plotId ORDER BY date DESC LIMIT 1")
    Mono<Weather> findLastWeatherByPlotId(@Param("plotId") Long plotId);
}