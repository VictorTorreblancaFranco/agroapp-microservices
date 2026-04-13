package pe.agro.farmservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.farmservice.model.Plot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlotRepository extends R2dbcRepository<Plot, Long> {

    Mono<Plot> findByCode(String code);

    Flux<Plot> findByUserId(Long userId);

    Flux<Plot> findByIsActiveTrue();

    Flux<Plot> findByIsActiveFalse();

    Mono<Boolean> existsByNameAndUserId(String name, Long userId);

    @Query("UPDATE plot SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> softDeleteById(@Param("id") Long id);

    @Query("UPDATE plot SET is_active = true, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> restoreById(@Param("id") Long id);
}