package pe.agro.sowingservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.sowingservice.model.Loss;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LossRepository extends R2dbcRepository<Loss, Long> {

    Flux<Loss> findBySowingId(Long sowingId);

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM loss WHERE sowing_id = :sowingId")
    Mono<Integer> sumQuantityBySowingId(@Param("sowingId") Long sowingId);
}