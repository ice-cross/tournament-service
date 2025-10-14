package pl.ick.tournament_service.utils.mappers;

import org.springframework.stereotype.Component;
import pl.ick.tournament_service.entity.Event;
import pl.ick.tournament_service.model.answer.GetEventAnswer;
import pl.ick.tournament_service.model.dto.EventDto;
import pl.ick.tournament_service.model.request.CreateEventRequest;

@Component
public class EventMapper {

    public Event toEntity(CreateEventRequest request) {
        return Event.builder()
                .name(request.name())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .location(request.location())
                .description(request.description())
                .build();
    }

    public EventDto toDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getDescription()
        );
    }

    public GetEventAnswer toGetAnswer(Event event) {
        return new GetEventAnswer(
                event.getId(),
                event.getName(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getDescription()
        );
    }
}
