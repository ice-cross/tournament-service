package pl.ick.tournament_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_service.model.dto.HeatDto;
import pl.ick.tournament_service.model.request.UpdateHeatResultRequest;
import pl.ick.tournament_service.service.HeatService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")//todo create a configuration for this
@RestController
@RequestMapping("/api/heats")
@RequiredArgsConstructor
public class HeatController {

    private final HeatService heatService;

    @PostMapping("/{tournamentId}/generate")
    public ResponseEntity<Void> generate(@PathVariable Long tournamentId) {
        heatService.generateHeatsForTournament(tournamentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<List<HeatDto>> getHeats(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(heatService.getHeats(tournamentId));
    }

    @PostMapping("/results")
    public ResponseEntity<HeatDto> updateResults(@RequestBody @Valid UpdateHeatResultRequest request) {
        return ResponseEntity.ok(heatService.updateHeatResults(request));
    }

    @GetMapping("/{tournamentId}/current-heat")
    public ResponseEntity<HeatDto> getCurrentHeat(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(heatService.getCurrentHeat(tournamentId));
    }
}

