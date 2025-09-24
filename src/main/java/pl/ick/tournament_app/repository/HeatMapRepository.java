package pl.ick.tournament_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_app.entity.HeatMap;

import java.util.List;

public interface HeatMapRepository extends JpaRepository<HeatMap, Long> {
    List<HeatMap> findByTypeOrderByRoundAscHeatNumberAscSlotNumberAsc(String type);
}
