package pl.ick.tournament_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_service.model.answer.CreateRiderAnswer;
import pl.ick.tournament_service.model.answer.EditRiderAnswer;
import pl.ick.tournament_service.model.dto.RiderDto;
import pl.ick.tournament_service.model.request.CreateRiderRequest;
import pl.ick.tournament_service.model.request.EditRiderRequest;
import pl.ick.tournament_service.model.request.RiderRegistrationRequest;
import pl.ick.tournament_service.service.RiderService;

import java.util.List;

@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
@Slf4j
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<CreateRiderAnswer> createRider(
            @RequestBody @Valid CreateRiderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(riderService.createRider(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerRider(@RequestBody RiderRegistrationRequest request){
        try {
            riderService.registerRider(request);
            return ResponseEntity.ok("Rider registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error occurred during registration: "+ e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RiderDto>> getAll() {
        return ResponseEntity.ok(riderService.getAllRiders());
    }

    @GetMapping("/confirmed/{tournamentId}")
    public ResponseEntity<List<RiderDto>> getConfirmedByTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(riderService.getConfirmedRidersDtoByTournament(tournamentId));
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
