package pe.agro.sowingservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.sowingservice.model.Cost;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CostRepository extends R2dbcRepository<Cost, Long> {

    Flux<Cost> findBySowingId(Long sowingId);

    @Query("SELECT COALESCE(SUM(amount), 0) FROM cost WHERE sowing_id = :sowingId")
    Mono<Double> sumAmountBySowingId(@Param("sowingId") Long sowingId);
}