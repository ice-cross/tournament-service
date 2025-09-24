package pl.ick.tournament_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_app.entity.Tournament;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByEventId(Long eventId);
}