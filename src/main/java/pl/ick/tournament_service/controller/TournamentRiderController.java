package pl.ick.tournament_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_service.service.TournamentRiderService;


@RestController
@RequestMapping("/api/tournament-riders")
@RequiredArgsConstructor
public class TournamentRiderController {

    private final TournamentRiderService riderTournamentService;

    @PostMapping("/confirm/{tournamentId}/{riderId}")
    public ResponseEntity<String> confirmRider(@PathVariable Long riderId, @PathVariable Long tournamentId) {
        riderTournamentService.confirmRiderForTournament(riderId, tournamentId);
        return ResponseEntity.ok("Rider confirmed successfully");
    }
}
