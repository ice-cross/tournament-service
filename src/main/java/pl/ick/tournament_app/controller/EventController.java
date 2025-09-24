package pl.ick.tournament_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_app.model.answer.CreateEventAnswer;
import pl.ick.tournament_app.model.answer.EditEventAnswer;
import pl.ick.tournament_app.model.answer.GetEventAnswer;
import pl.ick.tournament_app.model.dto.EventDto;
import pl.ick.tournament_app.model.request.CreateEventRequest;
import pl.ick.tournament_app.model.request.EditEventRequest;
import pl.ick.tournament_app.service.EventService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")//todo create a configuration for this
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventAnswer> createEvent(@RequestBody @Valid CreateEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(req));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> all() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEventAnswer> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditEventAnswer> editEvent(@PathVariable Long id,
                                                     @RequestBody @Valid EditEventRequest req) {
        return ResponseEntity.ok(eventService.editEvent(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}

