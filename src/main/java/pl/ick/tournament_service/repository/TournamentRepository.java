package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Tournament;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByEventId(Long eventId);
    List<Tournament> findByIdIn(List<Long> tournamentIds);
}