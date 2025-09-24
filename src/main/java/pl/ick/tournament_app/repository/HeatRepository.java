package pl.ick.tournament_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_app.entity.Heat;

import java.util.List;

public interface HeatRepository extends JpaRepository<Heat, Long> {
    List<Heat> findByTournamentId(Long tournamentId);
}
