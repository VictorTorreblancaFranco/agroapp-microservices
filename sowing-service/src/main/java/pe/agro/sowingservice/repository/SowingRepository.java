package pe.agro.sowingservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.agro.sowingservice.model.Sowing;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

@Repository
public interface SowingRepository extends R2dbcRepository<Sowing, Long> {

    @Query("SELECT * FROM sowing WHERE plot_id = :plotId")
    Flux<Sowing> findByPlotId(@Param("plotId") Long plotId);

    @Query("SELECT * FROM sowing WHERE crop_id = :cropId")
    Flux<Sowing> findByCropId(@Param("cropId") Long cropId);

    @Query("SELECT * FROM sowing WHERE status = :status")
    Flux<Sowing> findByStatus(@Param("status") String status);

    @Query("SELECT * FROM sowing WHERE plot_id = :plotId AND status = :status")
    Flux<Sowing> findByPlotIdAndStatus(@Param("plotId") Long plotId, @Param("status") String status);

    @Query("UPDATE sowing SET status = :status, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> updateStatus(@Param("id") Long id, @Param("status") String status);

    @Query("UPDATE sowing SET actual_harvest_date = :harvestDate, status = 'COMPLETED', updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    Mono<Integer> completeHarvest(@Param("id") Long id, @Param("harvestDate") LocalDate harvestDate);
}