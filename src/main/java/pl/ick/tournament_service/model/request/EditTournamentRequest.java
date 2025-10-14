package pl.ick.tournament_service.model.request;

import jakarta.validation.constraints.NotBlank;

public record EditTournamentRequest(
        @NotBlank String name
) {}