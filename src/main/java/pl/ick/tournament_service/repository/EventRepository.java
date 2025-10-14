package pl.ick.tournament_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ick.tournament_service.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
