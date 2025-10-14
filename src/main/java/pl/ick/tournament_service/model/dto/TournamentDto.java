package pl.ick.tournament_service.model.dto;

import pl.ick.tournament_service.model.TournamentType;

public record TournamentDto(
        Long id,
        String name,
        Long eventId,
        TournamentType type
) {}