package pl.ick.tournament_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_app.entity.AgeGroup;

import java.util.List;

public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long> {
    List<AgeGroup> findByTournamentId(Long tournamentId);
}