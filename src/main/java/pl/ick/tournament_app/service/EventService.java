package pl.ick.tournament_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_app.entity.Event;
import pl.ick.tournament_app.exceptions.EventNotFoundException;
import pl.ick.tournament_app.model.answer.CreateEventAnswer;
import pl.ick.tournament_app.model.answer.EditEventAnswer;
import pl.ick.tournament_app.model.answer.GetEventAnswer;
import pl.ick.tournament_app.model.dto.EventDto;
import pl.ick.tournament_app.model.request.CreateEventRequest;
import pl.ick.tournament_app.model.request.EditEventRequest;
import pl.ick.tournament_app.repository.EventRepository;
import pl.ick.tournament_app.utils.mappers.EventMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public CreateEventAnswer createEvent(CreateEventRequest request) {
        Event event = eventMapper.toEntity(request);
        Event saved = eventRepository.save(event);
        return new CreateEventAnswer(saved.getId(), "Event created successfully");
    }

    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetEventAnswer getEventInfo(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        return eventMapper.toGetAnswer(event);
    }

    public EditEventAnswer editEvent(Long eventId, EditEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        event.setName(request.name());
        event.setStartDate(request.startDate());
        event.setEndDate(request.endDate());
        event.setLocation(request.location());

        Event updated = eventRepository.save(event);

        return new EditEventAnswer(updated.getId(), "Event updated successfully");
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        eventRepository.deleteById(eventId);
    }
}