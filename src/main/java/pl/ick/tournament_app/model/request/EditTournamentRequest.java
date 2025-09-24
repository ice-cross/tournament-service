package pl.ick.tournament_app.model.request;

import jakarta.validation.constraints.NotBlank;

public record EditTournamentRequest(
        @NotBlank String name
) {}