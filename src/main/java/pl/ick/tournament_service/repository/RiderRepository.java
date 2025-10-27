package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Rider;

import java.util.List;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    long countByAgeGroupTournamentId(Long tournamentId);
    List<Rider> findDistinctByTournamentRiders_Tournament_IdAndTournamentRiders_ConfirmedTrue(Long tournamentId);
}