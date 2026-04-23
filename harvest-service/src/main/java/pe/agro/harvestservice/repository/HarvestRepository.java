package pe.agro.harvestservice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import pe.agro.harvestservice.model.Harvest;
import reactor.core.publisher.Flux;

@Repository
public interface HarvestRepository extends R2dbcRepository<Harvest, Long> {
    
    Flux<Harvest> findBySowingId(Long sowingId);
}
