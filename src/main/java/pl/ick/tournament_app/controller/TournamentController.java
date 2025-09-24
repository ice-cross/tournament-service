package pl.ick.tournament_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_app.model.answer.CreateTournamentAnswer;
import pl.ick.tournament_app.model.answer.EditTournamentAnswer;
import pl.ick.tournament_app.model.answer.GetTournamentAnswer;
import pl.ick.tournament_app.model.dto.HeatDto;
import pl.ick.tournament_app.model.dto.RiderDto;
import pl.ick.tournament_app.model.dto.TournamentDto;
import pl.ick.tournament_app.model.request.CreateTournamentRequest;
import pl.ick.tournament_app.model.request.EditTournamentRequest;
import pl.ick.tournament_app.service.TournamentService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")//todo create a configuration for this
@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<CreateTournamentAnswer> createTournament(
            @RequestBody @Valid CreateTournamentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.createTournament(request));
    }

    @GetMapping
    public ResponseEntity<List<TournamentDto>> getAll() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TournamentDto>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(tournamentService.getTournamentsByEvent(eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTournamentAnswer> getTournament(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditTournamentAnswer> editTournament(
            @PathVariable Long id,
            @RequestBody @Valid EditTournamentRequest request) {
        return ResponseEntity.ok(tournamentService.editTournament(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/winner")
    public ResponseEntity<RiderDto> getWinner(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getWinner(id));
    }
}
