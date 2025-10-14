package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Qualification;

import java.util.Optional;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    Optional<Qualification> findByTournamentId(Long tournamentId);
}
