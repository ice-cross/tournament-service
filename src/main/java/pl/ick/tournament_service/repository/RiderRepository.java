package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Rider;

import java.util.List;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    List<Rider> findByAgeGroupId(Long ageGroupId);
    long countByAgeGroupTournamentId(Long tournamentId);
    List<Rider> findByAgeGroupTournamentId(Long tournamentId);
    List<Rider> findByTournaments(List<Long> tournaments);
}