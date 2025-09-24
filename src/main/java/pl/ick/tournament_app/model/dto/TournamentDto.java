package pl.ick.tournament_app.model.dto;

import pl.ick.tournament_app.model.TournamentType;

public record TournamentDto(
        Long id,
        String name,
        Long eventId,
        TournamentType type
) {}