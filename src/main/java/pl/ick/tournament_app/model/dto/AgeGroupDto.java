package pl.ick.tournament_app.model.dto;

public record AgeGroupDto(
        Long id,
        String name,
        int minAge,
        int maxAge,
        Long tournamentId
) {}