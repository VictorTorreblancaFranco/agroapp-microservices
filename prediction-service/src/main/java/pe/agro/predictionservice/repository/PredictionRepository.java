package pe.agro.predictionservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pe.agro.predictionservice.model.Prediction;
import reactor.core.publisher.Flux;

@Repository
public interface PredictionRepository extends R2dbcRepository<Prediction, Long> {
    Flux<Prediction> findBySowingId(Long sowingId);
}
