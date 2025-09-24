package pl.ick.tournament_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_app.model.answer.CreateRiderAnswer;
import pl.ick.tournament_app.model.answer.EditRiderAnswer;
import pl.ick.tournament_app.model.answer.GetRiderAnswer;
import pl.ick.tournament_app.model.dto.RiderDto;
import pl.ick.tournament_app.model.request.CreateRiderRequest;
import pl.ick.tournament_app.model.request.EditRiderRequest;
import pl.ick.tournament_app.service.RiderService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")//todo create a configuration for this
@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<CreateRiderAnswer> createRider(
            @RequestBody @Valid CreateRiderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(riderService.createRider(request));
    }

    @GetMapping
    public ResponseEntity<List<RiderDto>> getAll() {
        return ResponseEntity.ok(riderService.getAllRiders());
    }

    @GetMapping("/age-group/{ageGroupId}")
    public ResponseEntity<List<RiderDto>> getByAgeGroup(@PathVariable Long ageGroupId) {
        return ResponseEntity.ok(riderService.getRidersByAgeGroup(ageGroupId));
    }

    @GetMapping("/tournaments/{tournamentId}")
    public ResponseEntity<List<RiderDto>> getByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(riderService.getRidersByTournament(tournamentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetRiderAnswer> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(riderService.getRiderInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditRiderAnswer> editRider(
            @PathVariable Long id,
            @RequestBody @Valid EditRiderRequest request) {
        return ResponseEntity.ok(riderService.editRider(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRider(@PathVariable Long id) {
        riderService.deleteRider(id);
        return ResponseEntity.noContent().build();
    }
}
