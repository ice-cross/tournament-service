package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.AgeGroup;
import pl.ick.tournament_service.entity.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByEventId(Long eventId);
    List<Tournament> findByIdIn(List<Long> tournamentIds);
    Optional<Tournament> findByEventNameAndAgeGroup(String eventName, AgeGroup ageGroup);
}