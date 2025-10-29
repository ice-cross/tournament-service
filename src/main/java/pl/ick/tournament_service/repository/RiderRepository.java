package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Rider;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    long countByAgeGroupTournamentId(Long tournamentId);
    List<Rider> findDistinctByTournamentRiders_Tournament_IdAndTournamentRiders_ConfirmedTrue(Long tournamentId);
    Optional<Rider> findByLastNameAndBirthDate(String lastName, LocalDate birthDate);
}