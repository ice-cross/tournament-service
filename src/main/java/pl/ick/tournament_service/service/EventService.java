package pl.ick.tournament_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ick.tournament_service.entity.Event;
import pl.ick.tournament_service.exceptions.AgeGroupNotFoundException;
import pl.ick.tournament_service.model.answer.CreateEventAnswer;
import pl.ick.tournament_service.model.answer.EditEventAnswer;
import pl.ick.tournament_service.model.answer.GetEventAnswer;
import pl.ick.tournament_service.model.dto.EventDto;
import pl.ick.tournament_service.model.request.CreateEventRequest;
import pl.ick.tournament_service.model.request.EditEventRequest;
import pl.ick.tournament_service.repository.EventRepository;
import pl.ick.tournament_service.utils.mappers.EventMapper;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public CreateEventAnswer createEvent(CreateEventRequest request) {
        try{
            Event event = eventMapper.toEntity(request);
            Event saved = eventRepository.save(event);
            return new CreateEventAnswer(saved.getId(), "Event created successfully");
        } catch (Exception e){
            throw new RuntimeException("Error during creating new Event: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            if(nonNull(events) && !events.isEmpty()) {
                return events
                        .stream()
                        .map(eventMapper::toDto)
                        .toList();
            } else {
                throw new AgeGroupNotFoundException("No Events found.");
            }
        }catch (Exception e) {
            throw new RuntimeException("Error during getting Events: {}", e);
        }
    }

    @Transactional(readOnly = true)
    public GetEventAnswer getEventInfo(Long eventId) {
        try {
            Event event = getEventById(eventId);
            return eventMapper.toGetAnswer(event);
        } catch (Exception e) {
            throw new RuntimeException("Error during getting Event: {}", e);
        }
    }

    public EditEventAnswer editEvent(Long eventId, EditEventRequest request) {
        try {
            Event event = getEventById(eventId);

            event.setName(request.name());
            event.setStartDate(request.startDate());
            event.setEndDate(request.endDate());
            event.setLocation(request.location());

            Event updated = eventRepository.save(event);

            return new EditEventAnswer(updated.getId(), "Event updated successfully");
        } catch (Exception e){
            throw new RuntimeException("Error during updating Event: {}", e);
        }


    }

    public void deleteEvent(Long eventId) {
        try {
            Event event = getEventById(eventId);
            eventRepository.delete(event);
        } catch (Exception e) {
            throw new RuntimeException("Error during deleting Event: {}", e) ;
        }
    }

    public Event getEventById(Long id) {
        try {
            return eventRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error during getting Event: {}", e) ;
        }
    }
}