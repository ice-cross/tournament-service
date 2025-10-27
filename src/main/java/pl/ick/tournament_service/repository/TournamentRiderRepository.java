package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.TournamentRider;

import java.util.Optional;

public interface TournamentRiderRepository extends JpaRepository<TournamentRider, Long> {
    Optional<TournamentRider> findByRiderIdAndTournamentId(Long riderId, Long tournamentId);
}
