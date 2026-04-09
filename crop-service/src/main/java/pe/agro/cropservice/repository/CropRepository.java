package pe.agro.cropservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pe.agro.cropservice.model.Crop;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CropRepository extends R2dbcRepository<Crop, Long> {

    Mono<Crop> findByName(String name);
    Flux<Crop> findByType(String type);
    Flux<Crop> findBySeason(String season);
    Flux<Crop> findByIsActiveTrue();
    Flux<Crop> findByIsActiveFalse();
    Mono<Boolean> existsByName(String name);
}