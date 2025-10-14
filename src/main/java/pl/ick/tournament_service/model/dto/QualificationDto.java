package pl.ick.tournament_service.model.dto;

import java.util.List;

public record QualificationDto(
        Long id,
        Long tournamentId,
        List<QualificationEntryDto> entries
) {}
