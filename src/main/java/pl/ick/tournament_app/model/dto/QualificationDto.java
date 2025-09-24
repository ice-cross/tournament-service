package pl.ick.tournament_app.model.dto;

import java.util.List;

public record QualificationDto(
        Long id,
        Long tournamentId,
        List<QualificationEntryDto> entries
) {}
