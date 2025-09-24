package pl.ick.tournament_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_app.model.dto.QualificationDto;
import pl.ick.tournament_app.model.dto.QualificationEntryDto;
import pl.ick.tournament_app.model.request.AddTimeRequest;
import pl.ick.tournament_app.service.QualificationService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")//todo create a configuration for this
@RestController
@RequestMapping("/api/qualifications")
@RequiredArgsConstructor
public class QualificationController {

    private final QualificationService qualificationService;

    @GetMapping("/{tournamentId}")
    public ResponseEntity<QualificationDto> getOrCreate(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(qualificationService.getOrCreateQualification(tournamentId));
    }

    @PostMapping("/time")
    public ResponseEntity<QualificationDto> addTime(
            @RequestBody @Valid AddTimeRequest request) {
        return ResponseEntity.ok(qualificationService.addTime(request));
    }

    @GetMapping("/{tournamentId}/ranking")
    public ResponseEntity<List<QualificationEntryDto>> ranking(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(qualificationService.getRanking(tournamentId));
    }
}

