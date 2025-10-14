package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Heat;

import java.util.List;

public interface HeatRepository extends JpaRepository<Heat, Long> {
    List<Heat> findByTournamentId(Long tournamentId);
}
