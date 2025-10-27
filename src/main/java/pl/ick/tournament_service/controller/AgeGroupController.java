package pl.ick.tournament_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ick.tournament_service.model.answer.CreateAgeGroupAnswer;
import pl.ick.tournament_service.model.answer.EditAgeGroupAnswer;
import pl.ick.tournament_service.model.answer.GetAgeGroupAnswer;
import pl.ick.tournament_service.model.dto.AgeGroupDto;
import pl.ick.tournament_service.model.request.CreateAgeGroupRequest;
import pl.ick.tournament_service.model.request.EditAgeGroupRequest;
import pl.ick.tournament_service.service.AgeGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/age-groups")
@RequiredArgsConstructor
public class AgeGroupController {

    private final AgeGroupService ageGroupService;

    @PostMapping
    public ResponseEntity<CreateAgeGroupAnswer> createAgeGroup(
            @RequestBody @Valid CreateAgeGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ageGroupService.createAgeGroup(request));
    }

    @GetMapping
    public ResponseEntity<List<AgeGroupDto>> getAll() {
        return ResponseEntity.ok(ageGroupService.getAllAgeGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAgeGroupAnswer> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(ageGroupService.getAgeGroupInfo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditAgeGroupAnswer> editAgeGroup(
            @PathVariable Long id,
            @RequestBody @Valid EditAgeGroupRequest request) {
        return ResponseEntity.ok(ageGroupService.editAgeGroup(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgeGroup(@PathVariable Long id) {
        ageGroupService.deleteAgeGroup(id);
        return ResponseEntity.noContent().build();
    }
}
