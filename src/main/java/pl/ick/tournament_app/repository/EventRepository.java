package pl.ick.tournament_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_app.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
